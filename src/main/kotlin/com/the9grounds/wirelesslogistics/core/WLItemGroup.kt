package com.the9grounds.wirelesslogistics.core

import com.the9grounds.wirelesslogistics.registries.Blocks
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

class WLItemGroup(label: String) : CreativeModeTab(label) {

    override fun makeIcon(): ItemStack = ItemStack(Blocks.NODE.item, 1)
}