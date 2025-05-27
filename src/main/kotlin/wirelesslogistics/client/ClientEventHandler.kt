package wirelesslogistics.client

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import wirelesslogistics.items.ReferenceCardItem
import wirelesslogistics.registries.client.RenderTypes

@OnlyIn(Dist.CLIENT)
object ClientEventHandler {

    @SubscribeEvent
    fun renderLevelStageEvent(event: RenderLevelStageEvent) {
        if (event.stage != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return
        }

        val player = Minecraft.getInstance().player ?: return

        val itemInHand = Minecraft.getInstance().player?.mainHandItem

        if (itemInHand === null || itemInHand.item !is ReferenceCardItem) {
            return
        }

        val item = itemInHand.item as ReferenceCardItem

        if (!item.hasBoundedBlock(itemInHand)) {
            return
        }

        val boundedBlockAndPos = item.getBoundedBlockAndPos(itemInHand) ?: return

        val distance = boundedBlockAndPos.second.distSqr(player.onPos)

        if (distance > 20) {
            return
        }

        val matrixStack = event.poseStack
        val bufferSource = Minecraft.getInstance().renderBuffers().bufferSource()
        val consumer = bufferSource.getBuffer(RenderTypes.BLOCK_HIGHLIGHT)

        matrixStack.pushPose()

        val view = Minecraft.getInstance().gameRenderer.mainCamera.position
        matrixStack.translate(-view.x, -view.y, -view.z)
        val x = boundedBlockAndPos.second.x.toFloat()
        val y = boundedBlockAndPos.second.y.toFloat()
        val z = boundedBlockAndPos.second.z.toFloat()

        val pose = matrixStack.last().pose()

        // Bottom face - 4 edges
        consumer.addVertex(pose, x, y, z).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x + 1f, y, z).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x + 1f, y, z).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x + 1f, y, z + 1f).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x + 1f, y, z + 1f).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x, y, z + 1f).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x, y, z + 1f).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x, y, z).setColor(1f,1f,1f,1f)
        
        // Top face - 4 edges
        consumer.addVertex(pose, x, y + 1f, z).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x + 1f, y + 1f, z).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x + 1f, y + 1f, z).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x + 1f, y + 1f, z + 1f).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x + 1f, y + 1f, z + 1f).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x, y + 1f, z + 1f).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x, y + 1f, z + 1f).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x, y + 1f, z).setColor(1f,1f,1f,1f)
        
        // 4 vertical edges connecting top and bottom faces
        consumer.addVertex(pose, x, y, z).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x, y + 1f, z).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x + 1f, y, z).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x + 1f, y + 1f, z).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x + 1f, y, z + 1f).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x + 1f, y + 1f, z + 1f).setColor(1f,1f,1f,1f)
        
        consumer.addVertex(pose, x, y, z + 1f).setColor(1f,1f,1f,1f)
        consumer.addVertex(pose, x, y + 1f, z + 1f).setColor(1f,1f,1f,1f)

        matrixStack.popPose();
        RenderSystem.disableDepthTest();
        bufferSource.endBatch(RenderTypes.BLOCK_HIGHLIGHT)
    }
}