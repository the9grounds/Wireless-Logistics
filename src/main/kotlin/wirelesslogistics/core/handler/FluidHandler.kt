package wirelesslogistics.core.handler

import net.minecraft.core.Direction
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import wirelesslogistics.blockentity.NodeBlockEntity

class FluidHandler(side: Direction, blockEntity: NodeBlockEntity): AbstractHandler(side, blockEntity), IFluidHandler {

    override fun tick() {
        TODO("Not yet implemented")
    }

    override fun onWorldChange() {
        TODO("Not yet implemented")
    }

    override val isConnectable: Boolean
        get() = TODO("Not yet implemented")

    override fun getTanks(): Int {
        TODO("Not yet implemented")
    }

    override fun getFluidInTank(tank: Int): FluidStack {
        TODO("Not yet implemented")
    }

    override fun getTankCapacity(tank: Int): Int {
        TODO("Not yet implemented")
    }

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean {
        TODO("Not yet implemented")
    }

    override fun fill(stack: FluidStack, action: IFluidHandler.FluidAction): Int {
        TODO("Not yet implemented")
    }

    override fun drain(stack: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        TODO("Not yet implemented")
    }

    override fun drain(tank: Int, action: IFluidHandler.FluidAction): FluidStack {
        TODO("Not yet implemented")
    }
}