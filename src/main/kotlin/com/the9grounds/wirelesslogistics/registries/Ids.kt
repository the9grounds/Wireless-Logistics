package com.the9grounds.wirelesslogistics.registries

import com.the9grounds.wirelesslogistics.WirelessLogistics
import net.minecraft.resources.ResourceLocation

object Ids {

    val NODE = id("node")
    

    private fun id(id: String): ResourceLocation = ResourceLocation(WirelessLogistics.ID, id)
}