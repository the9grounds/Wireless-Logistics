package com.the9grounds.wirelesslogistics.core.handler

import com.the9grounds.wirelesslogistics.blockentity.NodeBlockEntity
import net.minecraft.core.Direction

abstract class AbstractHandler(val side: Direction, val blockEntity: NodeBlockEntity) {
    abstract fun tick()
    
    abstract fun onWorldChange()
    
    abstract val isConnectable: Boolean
}