package com.imyvm.community.application.interaction.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.operation.RegionalSettingMenu
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import net.minecraft.server.network.ServerPlayerEntity

fun executeRegion(playerExecutor: ServerPlayerEntity, community: Community) {
    CommunityMenuOpener.open(playerExecutor) {syncId ->
        RegionalSettingMenu(
            syncId = syncId,
            playerExecutor = playerExecutor,
            community = community,
            scope = null
        )
    }
}

fun executeScope(playerExecutor: ServerPlayerEntity, community: Community, scope: GeoScope, isGeographic: Boolean) {
    if (isGeographic) {
        val communityRegion = community.getRegion()
        communityRegion?.let { PlayerInteractionApi.modifyScope(playerExecutor, it, scope.scopeName) }
        playerExecutor.closeHandledScreen()
    } else {
        CommunityMenuOpener.open(playerExecutor) { syncId ->
            RegionalSettingMenu(
                syncId = syncId,
                playerExecutor = playerExecutor,
                community = community,
                scope = scope
            )
        }
    }
}