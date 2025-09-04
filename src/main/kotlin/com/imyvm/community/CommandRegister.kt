package com.imyvm.community

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.iwg.ImyvmWorldGeo
import com.imyvm.iwg.application.resetSelection
import com.imyvm.iwg.application.startSelection
import com.imyvm.iwg.application.stopSelection
import com.imyvm.iwg.domain.Region
import com.imyvm.iwg.inter.api.ImyvmWorldGeoApi.createRegion
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import java.util.*
import java.util.concurrent.CompletableFuture

private val SHAPE_TYPE_SUGGESTION_PROVIDER: SuggestionProvider<ServerCommandSource> = SuggestionProvider { _, builder ->
    Region.Companion.GeoShapeType.entries
        .filter { it != Region.Companion.GeoShapeType.UNKNOWN }
        .forEach { builder.suggest(it.name.lowercase(Locale.getDefault())) }
    CompletableFuture.completedFuture(builder.build())
}

fun register(dispatcher: CommandDispatcher<ServerCommandSource>, registryAccess: CommandRegistryAccess) {
    dispatcher.register(
        literal("community")
            .then(
                literal("help")
                    .executes{ runHelpCommand(it) }
            )
            .then(
                literal("select")
                    .then(
                        literal("start")
                            .executes { runStartSelect(it) }
                    )
                    .then(
                        literal("stop")
                            .executes { runStopSelect(it) }
                    )
                    .then(
                        literal("reset")
                            .executes { runResetSelect(it) }
                    )
            )
            .then(
                literal("create")
                    .then(
                        argument("shapeType", StringArgumentType.word())
                            .suggests(SHAPE_TYPE_SUGGESTION_PROVIDER)
                            .then(
                                argument("name", StringArgumentType.word())
                                    .executes { runCreateCommunity(it) }
                            )
                    )
            )
    )
}

private fun runHelpCommand(context: CommandContext<ServerCommandSource>): Int {
    TODO()
}

private fun runStartSelect(context: CommandContext<ServerCommandSource>): Int {
    val player = context.source.player ?: return 0
    return startSelection(player)
}

private fun runStopSelect(context: CommandContext<ServerCommandSource>): Int {
    val player = context.source.player ?: return 0
    return stopSelection(player)
}

private fun runResetSelect(context: CommandContext<ServerCommandSource>): Int {
    val player = context.source.player ?: return 0
    return resetSelection(player)
}

private fun runCreateCommunity(context: CommandContext<ServerCommandSource>): Int {
    val player = context.source.player ?: return 0
    val name = StringArgumentType.getString(context, "name")
    val shapeName = StringArgumentType.getString(context, "shapeType").uppercase(Locale.getDefault())

    if (createRegion(player, name, shapeName) == 0) {
        player.sendMessage(Text.of("Failed to create community, while creating region."))
        return 0
    } else {
        val community = Community(
            id = 0,
            regionNumberId = ImyvmWorldGeo.data.getRegionList().lastOrNull()?.numberID,
            foundingTimeSeconds = System.currentTimeMillis() / 1000,
            member = hashMapOf(player.uuid to com.imyvm.community.domain.CommunityRole.OWNER),
            joinPolicy = com.imyvm.community.domain.CommunityJoinPolicy.OPEN,
            status = CommunityStatus.PENDING
        )
        WorldGeoCommunityAddon.data.addCommunity(community)
        player.sendMessage(Text.of("Community creation request sent successfully with ID: ${community.id}"))

        return 1
    }
}