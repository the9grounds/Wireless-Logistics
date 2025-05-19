package wirelesslogistics.core.handler

import appeng.api.networking.IGridNode
import appeng.api.networking.IInWorldGridNodeHost
import net.minecraft.core.Direction
import wirelesslogistics.blockentity.NodeBlockEntity

class AE2Handler(side: Direction, blockEntity: NodeBlockEntity) : AbstractHandler(side, blockEntity),
    IInWorldGridNodeHost {
    override fun tick() {
        // Do Nothing
    }

    override fun onWorldChange() {
        TODO("Not yet implemented")
    }

    override val isConnectable: Boolean
        get() = TODO("Not yet implemented")

    override fun getGridNode(dir: Direction?): IGridNode? {
        TODO("Not yet implemented")
    }
}