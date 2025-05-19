package wirelesslogistics.registries

import net.minecraft.client.resources.model.Material
import wirelesslogistics.WirelessLogistics
import wirelesslogistics.block.NodeBlock
import wirelesslogistics.core.BlockDefinition
import wirelesslogistics.integration.Mods
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object Blocks {
    val REGISTRY = DeferredRegister.createBlocks(WirelessLogistics.ID)

    val BLOCKS = mutableListOf<Block>()
    
    val NODE = REGISTRY.registerBlock(Ids.NODE.path) { registryName ->
        NodeBlock(BlockBehaviour.Properties.of())
    }
    
    fun init() {
        
    }

//    fun <T: Block> createBlock(id: ResourceLocation, factory: (BlockBehaviour.Properties) -> T): BlockDefinition<DeferredBlock<T>> {
//        val block = constructBlock( factory, id)
//
//        val item = Items.createItem(id) { properties -> BlockItem(block.get(), properties) }
//
//        return BlockDefinition(block, item.get())
//    }
//
//    fun <T: Block> createBlock(id: ResourceLocation, requiredMod: Mods, factory: (BlockBehaviour.Properties) -> T): BlockDefinition<DeferredBlock<T>> {
//        val block = constructBlock( factory, id)
//
//        val item = Items.createItem(id, { properties -> BlockItem(block.get(), properties) }, requiredMod)
//
//        return BlockDefinition(block, item.get())
//    }
//
//    private fun <T : Block> constructBlock(
//        factory: (BlockBehaviour.Properties) -> T,
//        id: ResourceLocation
//    ): DeferredBlock<T> {
//        val props = BlockBehaviour.Properties.of()
//
//        val block = factory(props)
//
//        BLOCKS.add(block)
//
//        return REGISTRY.register(id.path, Supplier {
//            block
//        })
//    }
}