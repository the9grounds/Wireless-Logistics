package com.the9grounds.wirelesslogistics.registries

import com.the9grounds.wirelesslogistics.WirelessLogistics
import com.the9grounds.wirelesslogistics.blockentity.NodeBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.registerObject

object BlockEntities {
    val REGISTRY = KDeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, WirelessLogistics.ID)
    
    val NODE by REGISTRY.registerObject("node") { BlockEntityType.Builder.of(::NodeBlockEntity, Blocks.NODE.block).build(null) }

    fun init() {
        
    }
}