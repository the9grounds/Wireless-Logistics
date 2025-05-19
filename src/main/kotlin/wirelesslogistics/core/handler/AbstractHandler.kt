package wirelesslogistics.core.handler

import wirelesslogistics.blockentity.NodeBlockEntity
import net.minecraft.core.Direction

abstract class AbstractHandler(val side: Direction, val blockEntity: NodeBlockEntity) {
    abstract fun tick()
    
    abstract fun onWorldChange()
    
    abstract val isConnectable: Boolean
}