package wirelesslogistics.registries.client

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat.Mode
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent
import net.neoforged.neoforge.registries.DeferredRegister
import wirelesslogistics.WirelessLogistics
import java.util.*

object RenderTypes {
    val BLOCK_HIGHLIGHT: RenderType = RenderType.create("block_highlight", DefaultVertexFormat.POSITION_COLOR, Mode.LINES, 256, true, false,
        RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER).setLineState(RenderStateShard.LineStateShard(
            OptionalDouble.of(4.0))).setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING).setTransparencyState(
            RenderStateShard.TRANSLUCENT_TRANSPARENCY).setOutputState(RenderStateShard.ITEM_ENTITY_TARGET).setDepthTestState(
            RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(
            RenderStateShard.COLOR_WRITE).setCullState(RenderStateShard.NO_CULL).createCompositeState(false))

    fun register(event: RegisterNamedRenderTypesEvent) {
//        event.register(ResourceLocation.fromNamespaceAndPath(WirelessLogistics.ID, "block_highlight"), RenderType.translucent(), BLOCK_HIGHLIGHT)
    }
}