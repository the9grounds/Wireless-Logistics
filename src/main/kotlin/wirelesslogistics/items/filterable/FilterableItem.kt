package wirelesslogistics.items.filterable

import net.minecraft.world.item.Item
import wirelesslogistics.api.Filterable

open class FilterableItem(props: Properties): Item(props), Filterable {
}