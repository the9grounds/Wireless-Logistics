package wirelesslogistics.containers

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ComponentItemHandler
import wirelesslogistics.items.ReferenceCardItem
import wirelesslogistics.registries.DataComponents

class ReferenceCardHolderInventory(size: Int, itemStack: ItemStack): ComponentItemHandler(itemStack, DataComponents.REFERENCE_INVENTORY_COMPONENT.get(), size) {
    override fun getSlotLimit(slot: Int): Int = 1

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return stack.item is ReferenceCardItem
    }
}