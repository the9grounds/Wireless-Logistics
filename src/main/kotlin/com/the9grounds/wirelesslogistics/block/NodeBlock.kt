package com.the9grounds.wirelesslogistics.block

import com.the9grounds.wirelesslogistics.blockentity.NodeBlockEntity
import com.the9grounds.wirelesslogistics.registries.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class NodeBlock(props: Properties) : Block(props), EntityBlock {
    override fun newBlockEntity(pos: BlockPos, blockState: BlockState): BlockEntity? = NodeBlockEntity(pos, blockState)

    override fun onNeighborChange(state: BlockState?, level: LevelReader?, pos: BlockPos?, neighbor: BlockPos?) {
        if (level == null) {
            return
        }

        level.getBlockEntity(pos, BlockEntities.NODE).orElse(null)?.neighborChanged()
    }

    override fun use(
        p_60503_: BlockState,
        p_60504_: Level,
        p_60505_: BlockPos,
        p_60506_: Player,
        p_60507_: InteractionHand,
        p_60508_: BlockHitResult
    ): InteractionResult {
        
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return if (level.isClientSide) {
            null
        } else {
            NodeBlockEntity.Ticker as BlockEntityTicker<T>
        }
    }
}