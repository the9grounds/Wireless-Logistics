package wirelesslogistics.registries

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID
import net.neoforged.neoforge.registries.DeferredRegister
import wirelesslogistics.WirelessLogistics
import java.util.function.Supplier

import thedarkcolour.kotlinforforge.neoforge.forge.getValue


object CreativeTab {
    val REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WirelessLogistics.ID)

    val CREATIVE_TAB by REGISTRY.register("wirelesslogistics", Supplier {
        CreativeModeTab.builder()
            .title(Component.literal("Wireless Logistics"))
            .icon { ->
                ItemStack(Blocks.NODE.get())
            }
            .displayItems { params, output ->
            output.accept(Items.DEV_CONNECT_ITEM.get())
            output.accept(Blocks.NODE.get())
            output.accept(Items.REFERENCE_CARD.get())
        }.build()
    })

    fun init() {

    }
}