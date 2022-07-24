package com.the9grounds.wirelesslogistics.blockentity

import com.the9grounds.wirelesslogistics.registries.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class NodeBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(BlockEntities.NODE, pos, blockState) {
    
}