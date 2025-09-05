package com.imyvm.community

import com.imyvm.community.CommunityConfig.Companion.APPLICATION_EXPIRE_HOURS
import com.imyvm.community.CommunityConfig.Companion.MIN_NUMBER_MEMBER_REALM
import com.imyvm.community.WorldGeoCommunityAddon.Companion.pendingOperations
import com.imyvm.community.application.chargeFromApplicator
import com.imyvm.community.application.handleApplicationBranches
import com.imyvm.community.application.initialApplication
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.community.domain.PendingOperationType
import com.imyvm.economy.EconomyMod
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
import java.util.*
import java.util.concurrent.CompletableFuture

private val SHAPE_TYPE_SUGGESTION_PROVIDER: SuggestionProvider<ServerCommandSource> = SuggestionProvider { _, builder ->
    Region.Companion.GeoShapeType.entries
        .filter { it != Region.Companion.GeoShapeType.UNKNOWN }
        .forEach { builder.suggest(it.name.lowercase(Locale.getDefault())) }
    CompletableFuture.completedFuture(builder.build())
}

private val COMMUNITY_TYPE_PROVIDER: SuggestionProvider<ServerCommandSource> = SuggestionProvider { _, builder ->
    listOf("manor", "realm").forEach { builder.suggest(it) }
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
                                argument("communityType", StringArgumentType.word())
                                    .suggests(COMMUNITY_TYPE_PROVIDER)
                                    .then(
                                        argument("name", StringArgumentType.greedyString())
                                            .executes { runCreateCommunity(it) }
                                    )
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

    val communityType = StringArgumentType.getString(context, "communityType").lowercase(Locale.getDefault())
    if (chargeFromApplicator(player, communityType) == 0) return 0

    val name = StringArgumentType.getString(context, "name")
    val shapeName = StringArgumentType.getString(context, "shapeType").uppercase(Locale.getDefault())
    if (createRegion(player, name, shapeName) == 0) {
        player.sendMessage(Translator.tr("community.create.region.error"))
        return 0
    }
    initialApplication(player, name)
    handleApplicationBranches(player, communityType)
    return 1

}