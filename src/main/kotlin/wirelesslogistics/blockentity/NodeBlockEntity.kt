package wirelesslogistics.blockentity

import wirelesslogistics.Logger
import wirelesslogistics.core.handler.AbstractHandler
import wirelesslogistics.core.handler.ItemHandler
import wirelesslogistics.registries.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import wirelesslogistics.core.Handlers
import wirelesslogistics.core.handler.PowerHandler

class NodeBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(BlockEntities.NODE.get(), pos, blockState) {
    val connectedEntities = mutableListOf<NodeBlockEntity>()
    val sideToHandlers = mutableMapOf<Direction, MutableMap<Handlers, AbstractHandler>>()
    var i = 0

    override fun onLoad() {
        super.onLoad()
        Direction.values().forEach { 
            sideToHandlers[it] = mutableMapOf(Pair(Handlers.Item, ItemHandler(it, this)), Pair(Handlers.Power, PowerHandler(it, this)))
        }
    }

    fun <T> getHandlerForSideForCapability(cap: BlockCapability<*, *>, side: Direction): T? {
        val handlersForSide = sideToHandlers[side]

        if (handlersForSide == null || handlersForSide.size == 0) {
            return null
        }

        for (handler in handlersForSide) {
            if (cap.equals(Capabilities.ItemHandler.BLOCK) && handler.key === Handlers.Item) {
                return handler.value as T
            }
            if (cap.equals(Capabilities.EnergyStorage.BLOCK) && handler.key === Handlers.Power) {
                return handler.value as T
            }
        }

        return null
    }

    fun serverTick(level: Level, pos: BlockPos, blockState: BlockState) {
        if (i % ((20 * 60) * 5) == 0) {
            Logger.info("5 minute tick?")
        }
        i++
        
        sideToHandlers.forEach {
            it.value.forEach { item ->
                item.value.tick()
            }
        }
    }

    fun neighborChanged() {
        sideToHandlers.forEach { 
            it.value.forEach { item ->
                item.value.onWorldChange()
            }
        }
    }

    fun isConnected(): Boolean {
        return connectedEntities.isNotEmpty()
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