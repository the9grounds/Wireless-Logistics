package com.the9grounds.wirelesslogistics.registries

import com.the9grounds.wirelesslogistics.WirelessLogistics
import com.the9grounds.wirelesslogistics.block.NodeBlock
import com.the9grounds.wirelesslogistics.core.BlockDefinition
import com.the9grounds.wirelesslogistics.integration.Mods
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.registerObject

object Blocks {
    val REGISTRY = KDeferredRegister.create(ForgeRegistries.BLOCKS, WirelessLogistics.ID)

    val BLOCKS = mutableListOf<Block>()
    
    val NODE = createBlock(Ids.NODE, Material.METAL) {
        NodeBlock(it.strength(.75f, 11f).sound(SoundType.METAL))
    }
    
    fun init() {
        
    }

    fun <T: Block> createBlock(id: ResourceLocation, material: Material, factory: (BlockBehaviour.Properties) -> T): BlockDefinition<T> {
        val block = constructBlock(material, factory, id)

        val item = Items.createItem(id) { properties -> BlockItem(block, properties) }

        return BlockDefinition(block, item)
    }

    fun <T: Block> createBlock(id: ResourceLocation, material: Material, requiredMod: Mods, factory: (BlockBehaviour.Properties) -> T): BlockDefinition<T> {
        val block = constructBlock(material, factory, id)

        val item = Items.createItem(id, { properties -> BlockItem(block, properties) }, requiredMod)

        return BlockDefinition(block, item)
    }

    private fun <T : Block> constructBlock(
        material: Material,
        factory: (BlockBehaviour.Properties) -> T,
        id: ResourceLocation
    ): T {
        val props = BlockBehaviour.Properties.of(material)

        val block = factory(props)

        BLOCKS.add(block)

        REGISTRY.registerObject(id.path) {
            block
        }
        return block
    }
}