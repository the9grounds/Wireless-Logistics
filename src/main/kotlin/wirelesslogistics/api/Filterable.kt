package wirelesslogistics.api

import net.minecraft.world.item.ItemStack

interface Filterable {
    // Nullable in the cases of if a filter is placed on a power, as an example
    // We won't have a stack to compare against
    fun canAccept(stack: WLGenericStack?, direction: FilterableDirection): Boolean
}