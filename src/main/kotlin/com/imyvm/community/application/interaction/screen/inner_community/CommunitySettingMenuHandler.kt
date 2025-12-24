package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.CommunityMemberListMenu
import com.imyvm.community.inter.screen.inner_community.CommunityRegionScopeMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runOpenSettingRegional(playerExecutor: ServerPlayerEntity, community: Community) {
    CommunityMenuOpener.open(playerExecutor) { syncId ->
        CommunityRegionScopeMenu(
            syncId = syncId,
            playerExecutor = playerExecutor,
            community = community,
            geographicFunctionType = com.imyvm.community.domain.GeographicFunctionType.SETTING_ADJUSTMENT
        )
    }
}

fun runOpenSettingPlayerTargeted(playerExecutor: ServerPlayerEntity, community: Community) {
    CommunityMenuOpener.open(playerExecutor) {syncId ->
        CommunityMemberListMenu(syncId, community, playerExecutor)
    }
}