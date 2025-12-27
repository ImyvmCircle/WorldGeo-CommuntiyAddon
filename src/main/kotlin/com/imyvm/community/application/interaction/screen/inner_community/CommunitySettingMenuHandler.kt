package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.CommunityMemberListMenu
import com.imyvm.community.inter.screen.inner_community.CommunityRegionScopeMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runOpenSettingRegional(playerExecutor: ServerPlayerEntity, community: Community, runBackCommunity: (ServerPlayerEntity) -> Unit) {
    CommunityMenuOpener.open(playerExecutor) { syncId ->
        CommunityRegionScopeMenu(
            syncId = syncId,
            playerExecutor = playerExecutor,
            community = community,
            geographicFunctionType = com.imyvm.community.domain.GeographicFunctionType.SETTING_ADJUSTMENT
        ) { runBackToSettingMenu(playerExecutor,community,runBackCommunity) }
    }
}

fun runOpenSettingPlayerTargeted(playerExecutor: ServerPlayerEntity, community: Community, runBackCommunity: (ServerPlayerEntity) -> Unit) {
    CommunityMenuOpener.open(playerExecutor) {syncId ->
        CommunityMemberListMenu(syncId, community, playerExecutor) { runBackToSettingMenu(playerExecutor, community, runBackCommunity) }
    }
}

private fun runBackToSettingMenu(
    player: ServerPlayerEntity,
    community: Community,
    runBackCommunity: (ServerPlayerEntity) -> Unit
) {
    runOpenSettingMenu(player, community, runBackCommunity)
}