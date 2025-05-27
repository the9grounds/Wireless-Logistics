package wirelesslogistics.registries

import wirelesslogistics.WirelessLogistics
import wirelesslogistics.integration.Mods
import wirelesslogistics.items.DevConnectItem
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import wirelesslogistics.items.ReferenceCardItem
import wirelesslogistics.items.datacomponents.DevConnectItemData

object Items {
    val REGISTRY = DeferredRegister.createItems(WirelessLogistics.ID)

    val DEV_CONNECT_ITEM = REGISTRY.registerItem(Ids.DEV_CONNECT_ITEM.path, ::DevConnectItem)
    val NODE = REGISTRY.registerItem(Ids.NODE.path, {props ->
        BlockItem(Blocks.NODE.get(), props)
    }, Item.Properties())

    // Filters

    // ReferenceCard
    val REFERENCE_CARD = REGISTRY.registerItem(Ids.REFERENCE_CARD.path, ::ReferenceCardItem)

//    val DEV_CONNECT_ITEM by createItem(Ids.DEV_CONNECT_ITEM) { properties -> DevConnectItem(properties) }
    
    fun init() {
        
    }

//    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T): DeferredItem<T> {
//        val item = constructItem(factory, id)
//        return REGISTRY.register(id.path, Supplier {
//            item
//        })
//    }
//
//    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, vararg requiredMod: Mods): DeferredItem<T> {
//        val item = constructItem(factory, id, *requiredMod)
//        return REGISTRY.register(id.path, Supplier {
//            item
//        })
//    }
//
////    fun <T: Item> createItemForMod(id: ResourceLocation, factory: (Item.Properties) -> T, vararg requiredMod: Mods): ReadOnlyProperty<Any?, T> {
////        return REGISTRY.register(id.path, Supplier{
////            constructItem(factory, id, *requiredMod)
////        })
////    }
//
//    private fun <T: Item> constructItem(
//        factory: () -> T,
//        id: ResourceLocation,
//    ): T {
//        val item = factory()
//
//        TAB_ITEMS.add(item)
//        ITEMS.add(item)
//
//        return item
//    }
//
//    private fun <T : Item> constructItem(
//        factory: (Item.Properties) -> T,
//        id: ResourceLocation,
//        vararg requiredMod: Mods
//    ): T {
//        val props = Item.Properties()
//
//        var shouldShow = true
//
//        requiredMod.forEach {
//            if (!it.isEnabled) {
//                shouldShow = false
//            }
//        }
//        val item = factory(props)
//
//        if (shouldShow) {
//            TAB_ITEMS.add(item)
//        }
//
//        ITEMS.add(item)
//
//        return item
//    }
//
//    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, registry: DeferredRegister.Items): DeferredItem<T> {
//        return registry.register(id.path, Supplier {
//            constructItem(factory, id)
//        })
//    }
//
//    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, registry: DeferredRegister.Items, vararg requiredMod: Mods): DeferredItem<T> {
//        return registry.register(id.path, Supplier {
//            constructItem(factory, id, *requiredMod)
//        })
//    }
}