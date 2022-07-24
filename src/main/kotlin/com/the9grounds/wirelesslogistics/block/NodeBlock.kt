package com.the9grounds.wirelesslogistics.block

import com.the9grounds.wirelesslogistics.blockentity.NodeBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class NodeBlock(props: Properties) : Block(props), EntityBlock {
    override fun newBlockEntity(pos: BlockPos, blockState: BlockState): BlockEntity? = NodeBlockEntity(pos, blockState)
}