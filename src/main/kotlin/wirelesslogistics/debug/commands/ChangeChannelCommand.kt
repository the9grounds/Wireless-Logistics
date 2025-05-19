package wirelesslogistics.debug.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import wirelesslogistics.blockentity.NodeBlockEntity
import wirelesslogistics.core.handler.ItemHandler
import wirelesslogistics.registries.BlockEntities
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object ChangeChannelCommand : ICommand {
    override fun call(server: MinecraftServer, data: CommandContext<CommandSourceStack?>, sender: CommandSourceStack?) {
        
    }

    override fun addArguments(builder: LiteralArgumentBuilder<CommandSourceStack>) {
        builder.then(
            Commands.argument("side", IntegerArgumentType.integer())
            .then(Commands.argument("firstChannel", IntegerArgumentType.integer(0, 15))
            .then(Commands.argument("secondChannel", IntegerArgumentType.integer(0, 15))
            .executes {
            val side = IntegerArgumentType.getInteger(it, "side")
            val firstChannel = IntegerArgumentType.getInteger(it, "firstChannel")
            val secondChannel = IntegerArgumentType.getInteger(it, "secondChannel")

            val entity = it.source!!.entity

            if (entity is ServerPlayer) {
                val pos = entity.position()
                val blockEntityPos = pos.subtract(0.0, 1.0, 0.0)

                val blockEntity = entity.level.getBlockEntity(BlockPos(blockEntityPos.x.toInt(), blockEntityPos.y.toInt(), blockEntityPos.z.toInt()), BlockEntities.NODE.get()).orElse(null)

                if (blockEntity != null && blockEntity is NodeBlockEntity) {
                    blockEntity.sideToHandlers[Direction.values()[side]]?.forEach { item ->
                        if (item is ItemHandler) {
                            item.channels = Pair(firstChannel, secondChannel)
                        }
                    }
                }
            }

            1
        })))
    }
}