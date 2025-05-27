package wirelesslogistics.items

import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase
import net.minecraft.world.level.block.state.BlockState
import wirelesslogistics.items.datacomponents.*
import wirelesslogistics.registries.DataComponents

class ReferenceCardItem(props: Properties): Item(props) {
    companion object {
        val CODEC = RecordCodecBuilder.create {
            it.group(
                ResourceLocation.CODEC.optionalFieldOf("referencedBlockType").forGetter(ReferenceCardItemData::referencedBlockTypeOptional),
                BlockPos.CODEC.optionalFieldOf("referencedBlockPos").forGetter(ReferenceCardItemData::referencedBlockPosOptional),
                Level.RESOURCE_KEY_CODEC.optionalFieldOf("dimension").forGetter(ReferenceCardItemData::dimensionOptional)
            ).apply(it, ReferenceCardItemData::fromOptionals)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, ReferenceCardItemData> = StreamCodec.composite(
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC),
            ReferenceCardItemData::referencedBlockTypeOptional,
            ByteBufCodecs.optional(BlockPos.STREAM_CODEC),
            ReferenceCardItemData::referencedBlockPosOptional,
            ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.DIMENSION)),
            ReferenceCardItemData::dimensionOptional,
            ReferenceCardItemData::fromOptionals
        )
    }

    override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {

        if (context.level.isClientSide) {
            return InteractionResult.sidedSuccess(context.level.isClientSide())
        }

        if (context.player?.isShiftKeyDown == true) {
            // Clear state

            val data = stack.getOrDefault(DataComponents.REFERENCE_CARD_ITEM, ReferenceCardItemData())

            if (data.referencedBlockPos !== null) {
                data.clear()

                stack.set(DataComponents.REFERENCE_CARD_ITEM, data)

                context.player?.inventoryMenu?.broadcastChanges()

                context.player?.sendSystemMessage(Component.literal("Referenced Item Cleared"))

                return InteractionResult.SUCCESS
            }

            return InteractionResult.PASS
        }

        val level = context.level as ServerLevel

        val block = level.getBlockState(context.clickedPos).block

        if (block === Blocks.AIR) {
            return InteractionResult.PASS
        }

        val newData = ReferenceCardItemData(BuiltInRegistries.BLOCK.getKey(block), context.clickedPos, level.dimension())

        stack.set(DataComponents.REFERENCE_CARD_ITEM, newData)

        context.player?.sendSystemMessage(Component.literal("Referenced Item Bound"))

        return InteractionResult.SUCCESS
    }

    fun hasBoundedBlock(stack: ItemStack): Boolean {
        val data = stack.getOrDefault(DataComponents.REFERENCE_CARD_ITEM, ReferenceCardItemData())

        return data.referencedBlockPos != null
    }

    fun getBoundedBlockAndPos(stack: ItemStack): Pair<BlockState, BlockPos>? {
        val data = stack.getOrDefault(DataComponents.REFERENCE_CARD_ITEM, ReferenceCardItemData())

        if (data.referencedBlockPos === null) {
            // Why?
            return null
        }

        val currentLevel = Minecraft.getInstance().level

        if (currentLevel === null || currentLevel.dimension() !== data.dimension) {
            return null
        }

        val blockState = currentLevel.getBlockState(data.referencedBlockPos!!) ?: return null

        return Pair(blockState, data.referencedBlockPos!!)
    }
}