package wirelesslogistics.api

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class WLGenericStack(val id: ResourceLocation, val amount: Int, val hash: Int) {
    companion object {
        fun fromItemStack(stack: ItemStack): WLGenericStack {
            return WLGenericStack(BuiltInRegistries.ITEM.getKey(stack.item), stack.count, ItemStack.hashItemAndComponents(stack))
        }

        fun fromFluidStack(stack: FluidStack): WLGenericStack {
            return WLGenericStack(BuiltInRegistries.FLUID.getKey(stack.fluid), stack.amount, FluidStack.hashFluidAndComponents(stack))
        }
    }
}