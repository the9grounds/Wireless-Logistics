package com.the9grounds.wirelesslogistics.debug.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.the9grounds.wirelesslogistics.blockentity.NodeBlockEntity
import com.the9grounds.wirelesslogistics.core.handler.ItemHandler
import com.the9grounds.wirelesslogistics.registries.BlockEntities
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object ConnectToOtherBlockEntityCommand : ICommand {
    override fun call(server: MinecraftServer, data: CommandContext<CommandSourceStack?>, sender: CommandSourceStack?) {
        
    }

    override fun addArguments(builder: LiteralArgumentBuilder<CommandSourceStack>) {
        builder.then(
            Commands.argument("x", IntegerArgumentType.integer())
            .then(Commands.argument("y", IntegerArgumentType.integer())
            .then(Commands.argument("z", IntegerArgumentType.integer())
            .executes {
                val x = IntegerArgumentType.getInteger(it, "x")
                val y = IntegerArgumentType.getInteger(it, "y")
                val z = IntegerArgumentType.getInteger(it, "z")
                

                val entity = it.source!!.entity
    
                if (entity is ServerPlayer) {
                    
                    val otherBlockEntity = entity.level.getBlockEntity(BlockPos(x, y, z), BlockEntities.NODE).orElse(null)
                    
                    if (otherBlockEntity == null || otherBlockEntity !is NodeBlockEntity) {
                        return@executes 1
                    }
                    
                    val pos = entity.position()
                    val blockEntityPos = pos.subtract(0.0, 1.0, 0.0)
    
                    val blockEntity = entity.level.getBlockEntity(BlockPos(blockEntityPos), BlockEntities.NODE).orElse(null)
    
                    if (blockEntity != null && blockEntity is NodeBlockEntity) {
                        blockEntity.connectedEntities.add(otherBlockEntity)
                        otherBlockEntity.connectedEntities.add(blockEntity)
                    }
                }

                1
        })))
    }
}