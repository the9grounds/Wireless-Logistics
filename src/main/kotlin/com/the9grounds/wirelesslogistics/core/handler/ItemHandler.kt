package com.the9grounds.wirelesslogistics.core.handler

import com.the9grounds.wirelesslogistics.blockentity.NodeBlockEntity
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler

class ItemHandler(side: Direction, blockEntity: NodeBlockEntity) : AbstractHandler(side, blockEntity), IItemHandler {
    
    // Extract - Insert
    // Channels 0 - 15, by color
    var channels: Pair<Int, Int> = Pair(0, 0)
    var priority: Pair<Int, Int> = Pair(0, 0)
    // Extract - Insert
    var enabled: Pair<Boolean, Boolean> = Pair(true, true)
    val visited = mutableSetOf<ItemHandler>()
    var cachedAcceptor: BlockEntity? = null
    var itemsPerOperation = 4

    override val isConnectable: Boolean
        get() = cachedAcceptor != null
    
    var sleep = 100
    
    init {
        val acceptor = blockEntity.level?.getBlockEntity(blockEntity.blockPos.relative(side))
        
        if (acceptor != null && acceptor.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent) {
            cachedAcceptor = acceptor
        }
    }
    
    fun acceptItem(stack: ItemStack, simulate: Boolean): ItemStack {
        // Collect map of block entities to handlers, sort by priority, make sure insert is enabled and hasn't been visited
        val handlers = mutableListOf<ItemHandler>()

        var totalCount = 0
    
        val self = this

        // Seems overly inefficient once a lot of nodes are created and with multiple handlers for each side
        blockEntity.connectedEntities.forEach {
            it.sideToHandlers.forEach { t, u ->
                u.forEach { item ->
                    if (item is ItemHandler && item.enabled.second && item != self && item.cachedAcceptor != null && item.channels.second == self.channels.first) {
                        if (!visited.contains(item)) {
                            handlers.add(item)
                        }

                        totalCount++
                    }
                }
            }
        }
        
        
        blockEntity.sideToHandlers.forEach { t, u ->
            u.forEach { item ->
                if (item is ItemHandler && item.enabled.second && item != self && item.cachedAcceptor != null && item.channels.second == self.channels.first) {
                    if (!visited.contains(item)) {
                        handlers.add(item)
                    }

                    totalCount++
                }
            }
        }
        
        val sorted = handlers.sortedWith(compareBy { it.priority.second })
        
        if (sorted.isEmpty() && totalCount > 0) {
            visited.clear()
        }
        
        var itemStack = stack.copy()
        
        val beforeCount = visited.size
        
        sorted.forEach { 
            val remaining = it.putItemInStorage(stack, simulate)
            
            visited.add(it)
            
            if (remaining.isEmpty) {
                itemStack = remaining
                return@forEach
            }
        }
        
        if (beforeCount + sorted.size == visited.size) {
            visited.clear()
        }
        
        return itemStack
    }
    
    fun putItemInStorage(stack: ItemStack, simulate: Boolean): ItemStack {
        // This is assuming the entity on this side is an item handler, so we won't even create this handler if it can't accept items
        if (cachedAcceptor == null) {
            cachedAcceptor = blockEntity.level?.getBlockEntity(blockEntity.blockPos.relative(side))
        }
        
        if (cachedAcceptor == null) {
            return stack
        }

        // Possibly notify block entity to get rid of this instance?
        // Maybe not for upgrades sake
        val capability = cachedAcceptor!!.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.opposite).orElse(null)
            ?: return stack

        var itemStack = stack.copy()

        for (i in 0 until capability.slots) {
            val itemInSlot = capability.getStackInSlot(i)
            
            if (!itemInSlot.isEmpty && !itemStack.sameItem(itemInSlot)) {
                continue
            }
            
            val remaining = capability.insertItem(i, itemStack, simulate)
            
            if (remaining.isEmpty) {
                itemStack = ItemStack.EMPTY
                break
            }
            
            itemStack.count = remaining.count
        }
        
        return itemStack
    }
    
    override fun onWorldChange() {
        val acceptor = blockEntity.level?.getBlockEntity(blockEntity.blockPos.relative(side))

        if (acceptor != null && acceptor.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent) {
            cachedAcceptor = acceptor
        }
    }
    
    override fun getSlots(): Int = 1

    override fun getStackInSlot(slot: Int): ItemStack = ItemStack.EMPTY

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        return acceptItem(stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY

    override fun getSlotLimit(slot: Int): Int = 64

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = true
    
    fun shouldTick(): Boolean {
        if (sleep > 0) {
            sleep--
            return false
        }
        
        if (cachedAcceptor == null) {
            sleep = 8 * 8
            return false
        }
        
        return true
    }

    override fun tick() {
        if (!shouldTick()) {
            return
        }

        val capability = cachedAcceptor!!.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.opposite).orElse(null)
            ?: return
        
        var amountRemaining = itemsPerOperation

        for (i in 0 until capability.slots) {
            val stack = capability.getStackInSlot(i)
            
            if (stack.isEmpty) {
                continue
            }
            
            val itemStackRemoved = capability.extractItem(i, amountRemaining, true)
            
            val leftOverSimulate = acceptItem(itemStackRemoved, true)
            
            if (itemStackRemoved.sameItem(leftOverSimulate) && leftOverSimulate.count == itemStackRemoved.count) {
                sleep = 8
                return
            }
            
            val leftOver = acceptItem(itemStackRemoved, false)
            
            if (leftOver.sameItem(itemStackRemoved)) {
                itemStackRemoved.count -= leftOver.count
            }
            
            amountRemaining -= itemStackRemoved.count
            
            capability.extractItem(i, itemStackRemoved.count, false)
            
            if (amountRemaining == 0) {
                sleep = 8
                return
            }
        }
        
        sleep = 8 * 4
    }
}