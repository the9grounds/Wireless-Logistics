package wirelesslogistics.registries

import net.minecraft.core.registries.Registries
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.registries.DeferredRegister
import wirelesslogistics.WirelessLogistics
import wirelesslogistics.core.Codecs
import wirelesslogistics.items.ReferenceCardItem

object DataComponents {
    val REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, WirelessLogistics.ID)

    val FILTER_INVENTORY_COMPONENT = REGISTRY.registerComponentType("filter_inventory") { builder ->
        builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    }

    val REFERENCE_INVENTORY_COMPONENT = REGISTRY.registerComponentType("reference_inventory") { builder ->
        builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    }

    val REFERENCE_CARD_HOLDER_COMPONENT = REGISTRY.registerComponentType("reference_card_holder_inventory") { builder ->
        builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    }

    val DEV_CONNECT_ITEM = REGISTRY.registerComponentType("dev_connect_item") { builder ->
        builder.persistent(Codecs.DEV_CONNECT_ITEM_CODEC).networkSynchronized(Codecs.DEV_CONNECT_ITEM_STEAM_CODEC)
    }

    val REFERENCE_CARD_ITEM = REGISTRY.registerComponentType("reference_card_item") { builder ->
        builder.persistent(ReferenceCardItem.CODEC).networkSynchronized(ReferenceCardItem.STREAM_CODEC)
    }

    fun init() {

    }
}