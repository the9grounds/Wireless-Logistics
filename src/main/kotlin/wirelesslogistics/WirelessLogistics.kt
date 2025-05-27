package wirelesslogistics

import wirelesslogistics.debug.CommandRegistry
import wirelesslogistics.integration.Integration
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.items.IItemHandler
import org.apache.logging.log4j.Level
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist
import wirelesslogistics.client.ClientEventHandler
import wirelesslogistics.events.EventHandler
import wirelesslogistics.registries.*
import wirelesslogistics.registries.client.RenderTypes

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(WirelessLogistics.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object WirelessLogistics {
    const val ID = "wirelesslogistics"

    init {
        DataComponents.REGISTRY.register(MOD_BUS)
        Items.REGISTRY.register(MOD_BUS)
        Blocks.REGISTRY.register(MOD_BUS)
        BlockEntities.REGISTRY.register(MOD_BUS)
        CreativeTab.REGISTRY.register(MOD_BUS)

        NeoForge.EVENT_BUS.addListener(::serverStarting)
        NeoForge.EVENT_BUS.register(EventHandler)

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(WirelessLogistics::onClientSetup)
                Minecraft.getInstance()
                MOD_BUS.addListener(RenderTypes::register)
                NeoForge.EVENT_BUS.register(ClientEventHandler)
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

    @SubscribeEvent
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            BlockEntities.NODE.get()) {blockEntity, side ->
            blockEntity.getHandlerForSideForCapability<IItemHandler>(Capabilities.ItemHandler.BLOCK, side)
        }
        event.registerBlockEntity(
            Capabilities.EnergyStorage.BLOCK,
            BlockEntities.NODE.get()) { be, side ->
            be.getHandlerForSideForCapability<IEnergyStorage>(Capabilities.EnergyStorage.BLOCK, side)
        }
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        Logger.log(Level.INFO, "Server starting...")
    }

    private fun serverStarting(event: ServerStartingEvent) {
        CommandRegistry.register(event.server.commands.dispatcher)
    }

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        Logger.log(Level.INFO, "Hello! This is working!")
    }
}