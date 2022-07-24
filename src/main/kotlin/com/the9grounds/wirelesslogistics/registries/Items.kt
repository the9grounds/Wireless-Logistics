package com.the9grounds.wirelesslogistics.registries

import com.the9grounds.wirelesslogistics.WirelessLogistics
import com.the9grounds.wirelesslogistics.core.CreativeTab
import com.the9grounds.wirelesslogistics.integration.Mods
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.registerObject
import kotlin.properties.ReadOnlyProperty

object Items {
    val REGISTRY = KDeferredRegister.create(ForgeRegistries.ITEMS, WirelessLogistics.ID)
    
    val ITEMS = mutableListOf<Item>()
    
    fun init() {
        
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T): T {
        val item = constructItem(factory, id)
        REGISTRY.registerObject(id.path) {
            item
        }
        
        return item
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, vararg requiredMod: Mods): T {
        val item = constructItem(factory, id, *requiredMod)
        REGISTRY.registerObject(id.path) {
            item
        }
        
        return item
    }

    fun <T: Item> createItemForMod(id: ResourceLocation, factory: (Item.Properties) -> T, vararg requiredMod: Mods): ReadOnlyProperty<Any?, T> {
        return REGISTRY.registerObject(id.path) {
            constructItem(factory, id, *requiredMod)
        }
    }
    
    private fun <T: Item> constructItem(
        factory: (Item.Properties) -> T,
        id: ResourceLocation,
    ): T {
        val props = Item.Properties().tab(CreativeTab.group)

        val item = factory(props)

        if (item.registryName != null) {
            item.registryName = id
        }

        ITEMS.add(item)

        return item
    }

    private fun <T : Item> constructItem(
        factory: (Item.Properties) -> T,
        id: ResourceLocation,
        vararg requiredMod: Mods
    ): T {
        val props = Item.Properties()
        
        var shouldShow = true
        
        requiredMod.forEach { 
            if (!it.isEnabled) {
                shouldShow = false
            }
        }
        
        if (shouldShow) {
            props.tab(CreativeTab.group)
        }

        val item = factory(props)

        if (item.registryName != null) {
            item.registryName = id
        }

        ITEMS.add(item)

        return item
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, registry: KDeferredRegister<Item>): ReadOnlyProperty<Any?, T> {
        return registry.registerObject(id.path) {
            constructItem(factory, id)
        }
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, registry: KDeferredRegister<Item>, vararg requiredMod: Mods): ReadOnlyProperty<Any?, T> {
        return registry.registerObject(id.path) {
            constructItem(factory, id, *requiredMod)
        }
    }
}