package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.GeographicFunctionType
import com.imyvm.community.domain.community.CommunityJoinPolicy
import com.imyvm.community.inter.screen.inner_community.CommunityMemberListMenu
import com.imyvm.community.inter.screen.inner_community.CommunityOperationMenu
import com.imyvm.community.inter.screen.inner_community.CommunityRegionScopeMenu
import com.imyvm.community.inter.screen.inner_community.operation_only.CommunityOperationAdvancementMenu
import com.imyvm.community.inter.screen.inner_community.operation_only.CommunityOperationAuditListMenu
import com.imyvm.community.inter.screen.inner_community.operation_only.CommunityOperationRenameMenuAnvil
import net.minecraft.server.network.ServerPlayerEntity

fun runOPRenameCommunity(player: ServerPlayerEntity, community: Community, runBackCommunity: (ServerPlayerEntity) -> Unit){
    CommunityOperationRenameMenuAnvil(player, community = community, runBackCommunity).open()
}

fun runOpManageMembers(player: ServerPlayerEntity, community: Community, runBackCommunity: ((ServerPlayerEntity) -> Unit)) {
    CommunityMenuOpener.open(player) { syncId ->
        CommunityMemberListMenu(
            syncId = syncId,
            community = community,
            playerExecutor = player
        ) { runBackToCommunityOperationMenu(player, community, runBackCommunity)}
    }
}

fun runOPAuditRequests(player: ServerPlayerEntity, community: Community, runBackCommunity: (ServerPlayerEntity) -> Unit) {
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationAuditListMenu(
            syncId = syncId,
            community = community,
            playerExecutor = player,
            page = 0
        ) { runBackToCommunityOperationMenu(player, community, runBackCommunity) }
    }
}

fun runOPAdvancement(player: ServerPlayerEntity, community: Community, runBackCommunity: (ServerPlayerEntity) -> Unit){
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationAdvancementMenu(syncId, community, player, runBackCommunity)
    }
}

fun runOPRegion(
    player: ServerPlayerEntity,
    community: Community,
    geographicFunctionType: GeographicFunctionType,
    runBackCommunity: (ServerPlayerEntity) -> Unit
) {
    CommunityMenuOpener.open(player) { syncId ->
        CommunityRegionScopeMenu(
            syncId = syncId,
            playerExecutor = player,
            community = community,
            geographicFunctionType = geographicFunctionType
        ) { runBackToCommunityOperationMenu(player, community, runBackCommunity) }
    }
}

fun runOPChangeJoinPolicy(player: ServerPlayerEntity,community: Community, policy: CommunityJoinPolicy, runBackCommunity: (ServerPlayerEntity) -> Unit) {
    community.joinPolicy = when (policy) {
        CommunityJoinPolicy.OPEN -> CommunityJoinPolicy.APPLICATION
        CommunityJoinPolicy.APPLICATION -> CommunityJoinPolicy.INVITE_ONLY
        CommunityJoinPolicy.INVITE_ONLY -> CommunityJoinPolicy.OPEN
    }
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationMenu(syncId, community, player, runBackCommunity)
    }
}

private fun runBackToCommunityOperationMenu(player: ServerPlayerEntity, community: Community, runBackCommunity: (ServerPlayerEntity) -> Unit) {
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationMenu(syncId, community, player, runBackCommunity)
    }
}