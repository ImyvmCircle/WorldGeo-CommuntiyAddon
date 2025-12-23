package com.imyvm.community.application.interaction.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationTeleportPointMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import net.minecraft.server.network.ServerPlayerEntity

fun runSettingTeleportPoint(playerExecutor: ServerPlayerEntity, community: Community, scope: GeoScope) {
    val region = community.getRegion()
    if (region != null) {
        PlayerInteractionApi.addTeleportPoint(playerExecutor, region, scope)
    } else {
        playerExecutor.closeHandledScreen()
        playerExecutor.sendMessage(Translator.tr("community.not_found.region"))
    }
}

fun runResetTeleportPoint(playerExecutor: ServerPlayerEntity, community: Community, scope: GeoScope) {
    val region = community.getRegion()
    if (region != null) {
        PlayerInteractionApi.resetTeleportPoint(playerExecutor, region, scope)
    } else {
        playerExecutor.closeHandledScreen()
        playerExecutor.sendMessage(Translator.tr("community.not_found.region"))
    }
}

fun runToggleTeleportPointAccessibility(playerExecutor: ServerPlayerEntity, community: Community, scope: GeoScope) {
    val region = community.getRegion()
    if (region != null) {
        PlayerInteractionApi.toggleTeleportPointAccessibility(scope)
        CommunityMenuOpener.open(playerExecutor) { syncId ->
            CommunityOperationTeleportPointMenu(
                syncId = syncId,
                playerExecutor = playerExecutor,
                community = community,
                scope = scope
            )
        }
    } else {
        playerExecutor.closeHandledScreen()
        playerExecutor.sendMessage(Translator.tr("community.not_found.region"))
    }
}

fun runInquiryTeleportPoint(playerExecutor: ServerPlayerEntity, community: Community, scope: GeoScope) {
    val region = community.getRegion()
    if (region != null) {
        val blockPos = PlayerInteractionApi.getTeleportPoint(scope)
        if (blockPos != null) {
            playerExecutor.sendMessage(
                Translator.tr(
                    "community.teleport_point.inquiry.success.result",
                    blockPos.x,
                    blockPos.y,
                    blockPos.z
                )
            )
        } else {
            playerExecutor.sendMessage(Translator.tr("community.teleport_point.inquiry.success.no_point"))
        }
    } else {
        playerExecutor.closeHandledScreen()
        playerExecutor.sendMessage(Translator.tr("community.not_found.region"))
    }
}

fun runTeleportToPoint(playerExecutor: ServerPlayerEntity, community: Community, scope: GeoScope) {
    val region = community.getRegion()
    if (region != null) {
        PlayerInteractionApi.teleportPlayerToScope(playerExecutor, region, scope)
    } else {
        playerExecutor.closeHandledScreen()
        playerExecutor.sendMessage(Translator.tr("community.not_found.region"))
    }
}