package com.the9grounds.wirelesslogistics.blockentity

import com.the9grounds.wirelesslogistics.Logger
import com.the9grounds.wirelesslogistics.core.handler.AbstractHandler
import com.the9grounds.wirelesslogistics.core.handler.ItemHandler
import com.the9grounds.wirelesslogistics.registries.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler

class NodeBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(BlockEntities.NODE, pos, blockState) {
    val connectedEntities = mutableListOf<NodeBlockEntity>()
    val sideToHandlers = mutableMapOf<Direction, MutableList<AbstractHandler>>()
    var i = 0

    override fun onLoad() {
        super.onLoad()
        Direction.values().forEach { 
            sideToHandlers[it] = mutableListOf(ItemHandler(it, this))
        }
    }

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        val handlersForSide = sideToHandlers[side]
        
        if (handlersForSide == null || handlersForSide.size == 0) {
            return super.getCapability(cap, side) 
        }
        
        for (handler in handlersForSide) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && handler is ItemHandler) {
                return LazyOptional.of { handler as T }
            }
        }
        
        return super.getCapability(cap, side)
    }

    fun serverTick(level: Level, pos: BlockPos, blockState: BlockState) {
        if (i % ((20 * 60) * 5) == 0) {
            Logger.info("5 minute tick?")
        }
        i++
        
        sideToHandlers.forEach {
            it.value.forEach { item ->
                item.tick()
            }
        }
    }

    fun neighborChanged() {
        sideToHandlers.forEach { 
            it.value.forEach { item ->
                item.onWorldChange()
            }
        }
    }

    override fun setChanged() {
        super.setChanged()
    }
    
    object Ticker : BlockEntityTicker<NodeBlockEntity> {
        override fun tick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: NodeBlockEntity) {
           blockEntity.serverTick(level, blockPos, blockState)
        }
    }
}