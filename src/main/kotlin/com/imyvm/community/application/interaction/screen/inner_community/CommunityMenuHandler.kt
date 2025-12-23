package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.CommunityOperationMenu
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import com.imyvm.iwg.util.text.Translator
import net.minecraft.server.network.ServerPlayerEntity

fun runOpenOperationMenu(player: ServerPlayerEntity, community: Community) {
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationMenu(syncId, community, player)
    }
}

fun runTeleportCommunity(player: ServerPlayerEntity, community: Community) {
    player.closeHandledScreen()

    val region = community.getRegion()
    if (region == null) {
        player.sendMessage(Translator.tr("ui.community.button.interaction.teleport.execution.error.no_region"))
        return
    }

    val mainScope = region.geometryScope.firstOrNull()
    if (mainScope == null) {
        player.sendMessage(Translator.tr("ui.community.button.interaction.teleport.execution.error.no_scope"),)
        return
    }

    PlayerInteractionApi.teleportPlayerToScope(player, region, mainScope)
}