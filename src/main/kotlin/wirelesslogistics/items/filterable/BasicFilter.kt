package wirelesslogistics.items.filterable

import wirelesslogistics.api.FilterableDirection
import wirelesslogistics.api.WLGenericStack

// Basic filter for items, fluids, chemicals
class BasicFilter(props: Properties): FilterableItem(props) {
    companion object {
        val SLOTS = 10

//        fun getInventory()
    }

    override fun canAccept(stack: WLGenericStack?, direction: FilterableDirection): Boolean {
        return false
    }
}