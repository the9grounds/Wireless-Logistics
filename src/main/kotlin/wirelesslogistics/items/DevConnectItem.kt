package wirelesslogistics.items

import wirelesslogistics.block.NodeBlock
import wirelesslogistics.blockentity.NodeBlockEntity
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import wirelesslogistics.core.enums.DevConnectItemMode
import wirelesslogistics.items.datacomponents.DevConnectItemData
import wirelesslogistics.registries.DataComponents

class DevConnectItem(props: Properties) : Item(props.component(DataComponents.DEV_CONNECT_ITEM, DevConnectItemData())) {
    override fun useOn(useContext: UseOnContext): InteractionResult {
        if (useContext.level !is ServerLevel) {
            return super.useOn(useContext)
        }

        var itemData = useContext.itemInHand.get(DataComponents.DEV_CONNECT_ITEM)

        if (itemData === null) {
            itemData = DevConnectItemData(DevConnectItemMode.LINK, null)

            useContext.itemInHand.set(DataComponents.DEV_CONNECT_ITEM, itemData)
        }

        if (itemData.mode === DevConnectItemMode.LINK) {
            return handleLink(itemData, useContext.clickedPos, useContext.itemInHand, useContext.level, useContext.player!!)
        }

        if (itemData.mode === DevConnectItemMode.CHANGE) {

        }

        return super.useOn(useContext)
    }

    fun handleLink(itemData: DevConnectItemData, clickedPos: BlockPos, stack: ItemStack, level: Level, player: Player): InteractionResult {
        val blockEntity = level.getBlockEntity(clickedPos)

        if (blockEntity == null || blockEntity !is NodeBlockEntity) {
            return InteractionResult.PASS
        }


        if (itemData.linkedBlockPos == null) {
            itemData.linkedBlockPos = blockEntity.blockPos
            stack.set(DataComponents.DEV_CONNECT_ITEM, itemData)

            player.sendSystemMessage(Component.literal("Saved block entity"))

            return InteractionResult.SUCCESS
        }

        val blockPos = itemData.linkedBlockPos!!

        val other = level.getBlockEntity(blockPos)

        if (other == null || other !is NodeBlockEntity) {
            return InteractionResult.PASS
        }

        other.connectedEntities.add(blockEntity)
        blockEntity.connectedEntities.add(other)

        itemData.linkedBlockPos = null

        stack.set(DataComponents.DEV_CONNECT_ITEM, itemData)

        player.sendSystemMessage(Component.literal("Connected block entity"))

        return InteractionResult.SUCCESS
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {

        val itemData = stack.get(DataComponents.DEV_CONNECT_ITEM)

        if (itemData !== null && itemData.linkedBlockPos != null) {
            val blockPos = itemData.linkedBlockPos!!

            tooltipComponents.add(Component.literal("First Block Entity: x:${blockPos.x}, y:${blockPos.y}, z:${blockPos.z}"))
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
    }

    fun changeMode(
        stack: ItemStack,
        mode: DevConnectItemMode
    ) {
        var itemData = stack.get(DataComponents.DEV_CONNECT_ITEM)

        if (itemData === null) {
            itemData = DevConnectItemData(DevConnectItemMode.LINK, null)
        }

        itemData.mode = mode

        stack.set(DataComponents.DEV_CONNECT_ITEM, itemData)
    }

    fun getMode(stack: ItemStack): DevConnectItemMode {
        var itemData = stack.get(DataComponents.DEV_CONNECT_ITEM)

        if (itemData === null) {
            itemData = DevConnectItemData(DevConnectItemMode.LINK, null)

            stack.set(DataComponents.DEV_CONNECT_ITEM, itemData)
        }

        return itemData.mode
    }
}