package wirelesslogistics.items.filterable

import net.minecraft.world.item.Item.Properties
import wirelesslogistics.api.Filterable
import wirelesslogistics.api.FilterableDirection
import wirelesslogistics.api.WLGenericStack

class OrFilterItem(props: Properties): FilterableItem(props) {
    companion object {
        val SLOTS = 10
    }

    override fun canAccept(stack: WLGenericStack?, direction: FilterableDirection): Boolean {
        return false
    }
}