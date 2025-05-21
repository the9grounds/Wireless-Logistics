package wirelesslogistics.containers

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ComponentItemHandler
import wirelesslogistics.registries.DataComponents

class FilterInventory(size: Int, itemStack: ItemStack): ComponentItemHandler(itemStack, DataComponents.FILTER_INVENTORY_COMPONENT.get(), size) {
    override fun getSlotLimit(slot: Int): Int {
        return 1
    }
}