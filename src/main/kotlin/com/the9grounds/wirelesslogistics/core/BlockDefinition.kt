package com.the9grounds.wirelesslogistics.core

import net.minecraft.world.item.BlockItem

data class BlockDefinition<T>(val block: T, val item: BlockItem)
