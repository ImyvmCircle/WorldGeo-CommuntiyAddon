package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.GeographicFunctionType
import com.imyvm.community.domain.community.CommunityJoinPolicy
import com.imyvm.community.inter.screen.inner_community.CommunityOperationMenu
import com.imyvm.community.inter.screen.inner_community.CommunityRegionScopeMenu
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationAdvancementMenu
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationAuditListMenu
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationMemberListMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runOPRenameCommunity(player: ServerPlayerEntity, community: Community){
    CommunityMenuOpener.openCommunityRenameAnvilMenu(player, community)
}

fun runOpManageMembers(player: ServerPlayerEntity, community: Community){
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationMemberListMenu(syncId, community, player)
    }
}

fun runOPAuditRequests(player: ServerPlayerEntity, community: Community) {
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationAuditListMenu(syncId, community, player, 0)
    }
}

fun runOPAdvancement(player: ServerPlayerEntity, community: Community){
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationAdvancementMenu(syncId, community, player)
    }
}

fun runOPRegion(player: ServerPlayerEntity, community: Community, geographicFunctionType: GeographicFunctionType) {
    CommunityMenuOpener.open(player) { syncId ->
        CommunityRegionScopeMenu(
            syncId = syncId,
            playerExecutor = player,
            community = community,
            geographicFunctionType = geographicFunctionType
        )
    }
}

fun runOPChangeJoinPolicy(player: ServerPlayerEntity,community: Community, policy: CommunityJoinPolicy) {
    community.joinPolicy = when (policy) {
        CommunityJoinPolicy.OPEN -> CommunityJoinPolicy.APPLICATION
        CommunityJoinPolicy.APPLICATION -> CommunityJoinPolicy.INVITE_ONLY
        CommunityJoinPolicy.INVITE_ONLY -> CommunityJoinPolicy.OPEN
    }
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationMenu(syncId, community, player)
    }
}