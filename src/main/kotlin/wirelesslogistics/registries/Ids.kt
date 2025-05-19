package wirelesslogistics.registries

import wirelesslogistics.WirelessLogistics
import net.minecraft.resources.ResourceLocation

object Ids {

    val NODE = id("node")

    val DEV_CONNECT_ITEM = id("dev_connect_item")
    

    private fun id(id: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(WirelessLogistics.ID, id)
}