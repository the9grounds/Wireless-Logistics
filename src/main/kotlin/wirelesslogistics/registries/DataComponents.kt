package wirelesslogistics.registries

import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister
import wirelesslogistics.WirelessLogistics
import wirelesslogistics.core.Codecs

object DataComponents {
    val REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, WirelessLogistics.ID)

    val DEV_CONNECT_ITEM = REGISTRY.registerComponentType("dev_connect_item") { builder ->
        builder.persistent(Codecs.DEV_CONNECT_ITEM_CODEC).networkSynchronized(Codecs.DEV_CONNECT_ITEM_STEAM_CODEC)
    }

    fun init() {

    }
}