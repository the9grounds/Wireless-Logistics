package com.the9grounds.wirelesslogistics

import com.the9grounds.wirelesslogistics.integration.Integration
import com.the9grounds.wirelesslogistics.registries.BlockEntities
import com.the9grounds.wirelesslogistics.registries.Blocks
import com.the9grounds.wirelesslogistics.registries.Items
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.Level
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(WirelessLogistics.ID)
object WirelessLogistics {
    const val ID = "wirelesslogistics"

    init {
        Items.REGISTRY.register(MOD_BUS)
        Blocks.REGISTRY.register(MOD_BUS)
        BlockEntities.REGISTRY.register(MOD_BUS)
        Items.init()
        Blocks.init()
        BlockEntities.init()

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(WirelessLogistics::onClientSetup)
                Minecraft.getInstance()
            },
            serverTarget = {
                MOD_BUS.addListener(WirelessLogistics::onServerSetup)
                "test"
            })

        println(obj)
        Integration.init()
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        Logger.log(Level.INFO, "Initializing client...")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        Logger.log(Level.INFO, "Server starting...")
    }
}