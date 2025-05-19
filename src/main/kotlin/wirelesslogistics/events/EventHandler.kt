package wirelesslogistics.events

import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.InputEvent.MouseScrollingEvent

object EventHandler {
    @SubscribeEvent
    fun mouseScrollEvent(e: MouseScrollingEvent) {
        if (Minecraft.getInstance().player!!.isShiftKeyDown) {

        }
    }
}