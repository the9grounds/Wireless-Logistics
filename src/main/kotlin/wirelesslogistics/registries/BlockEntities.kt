package wirelesslogistics.registries

import net.minecraft.core.registries.Registries
import wirelesslogistics.WirelessLogistics
import wirelesslogistics.blockentity.NodeBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object BlockEntities {
    val REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, WirelessLogistics.ID)
    
    val NODE = REGISTRY.register("node", Supplier { BlockEntityType.Builder.of(::NodeBlockEntity, Blocks.NODE.get()).build(null) })

    fun init() {
        
    }
}