package com.imyvm.community

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

fun register(dispatcher: CommandDispatcher<ServerCommandSource>, registryAccess: CommandRegistryAccess) {
    dispatcher.register(
        literal("community")
            .then(
                literal("help")
                    .executes{ runHelpCommand(it) }
            )

    )
}

private fun runHelpCommand(context: CommandContext<ServerCommandSource>): Int {
    TODO()
}