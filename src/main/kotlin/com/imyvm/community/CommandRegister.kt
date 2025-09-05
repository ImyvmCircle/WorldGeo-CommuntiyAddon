package com.imyvm.community

import CommunityDatabase.Companion.communities
import com.imyvm.community.CommunityConfig.Companion.APPLICATION_EXPIRE_HOURS
import com.imyvm.community.CommunityConfig.Companion.IS_CHECKING_MANOR_MEMBER_SIZE
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
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
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
            .then(
                literal("join")
                    .then(
                        argument("name", StringArgumentType.greedyString())
                            .executes{ runJoinByName(it) }
                    )
                    .then(
                        argument("id", IntegerArgumentType.integer())
                            .executes{ runJoinById(it) }
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

private fun runJoinByName(context: CommandContext<ServerCommandSource>): Int{
    val player = context.source.player ?: return 0

    val name = StringArgumentType.getString(context, "name")
    val targetRegion = ImyvmWorldGeo.data.getRegionList().find {
        it.name.equals(name, ignoreCase = true)
    } ?: run {
        player.sendMessage(Translator.tr("community.join.error.notfound.name", name))
        return 0
    }
    val targetCommunity = communities.find {
        it.regionNumberId == targetRegion.numberID
    } ?:run {
        player.sendMessage(Translator.tr("community.join.error.notfound.name", name))
        return 0
    }

    return runJoin(player, targetCommunity)
}

private fun runJoinById(context: CommandContext<ServerCommandSource>): Int {
    val player = context.source.player ?: return 0

    val id = IntegerArgumentType.getInteger(context, "id")
    val targetCommunity = communities.find {
        it.id == id
    } ?: run {
        player.sendMessage(Translator.tr("community.join.error.notfound.id", id))
        return 0
    }
    return runJoin(player, targetCommunity)
}

private fun runJoin(player: ServerPlayerEntity, targetCommunity: Community): Int{
    if (IS_CHECKING_MANOR_MEMBER_SIZE.value) {
        if ((targetCommunity.status == CommunityStatus.ACTIVE_MANOR  || targetCommunity.status == CommunityStatus.PENDING_MANOR) &&
            targetCommunity.member.count { it.value != com.imyvm.community.domain.CommunityRole.APPLICANT } >= MIN_NUMBER_MEMBER_REALM.value) {
            player.sendMessage(Translator.tr("community.join.error.full", MIN_NUMBER_MEMBER_REALM.value))
            return 0
        }
    }

    when (targetCommunity.joinPolicy) {
        com.imyvm.community.domain.CommunityJoinPolicy.OPEN -> {
            targetCommunity.member[player.uuid] = com.imyvm.community.domain.CommunityRole.MEMBER
            player.sendMessage(Translator.tr("community.join.success", targetCommunity.id))
            return 1
        }
        com.imyvm.community.domain.CommunityJoinPolicy.APPLICATION -> {
            if (targetCommunity.member.containsKey(player.uuid)) {
                player.sendMessage(Translator.tr("community.join.error.already_applied", targetCommunity.id))
                return 0
            }
            targetCommunity.member[player.uuid] = com.imyvm.community.domain.CommunityRole.APPLICANT
            player.sendMessage(targetCommunity.getRegion()
                ?.let { Translator.tr("community.join.applied", it.name ,targetCommunity.id) })
            return 1
        }
        com.imyvm.community.domain.CommunityJoinPolicy.INVITE_ONLY -> {
            player.sendMessage(Translator.tr("community.join.error.invite_only", targetCommunity.id))
            return 0
        }
    }
}