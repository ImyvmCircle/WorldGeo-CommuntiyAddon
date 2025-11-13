package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationAdvancementMenu
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationAuditListMenu
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationMemberListMenu
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationRegionMenu
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

fun runOPRegion(player: ServerPlayerEntity, community: Community, isGeographic: Boolean) {
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationRegionMenu(syncId, community, isGeographic, player)
    }
}

fun runOPAdvancement(player: ServerPlayerEntity, community: Community){
    CommunityMenuOpener.open(player) { syncId ->
        CommunityOperationAdvancementMenu(syncId, community, player)
    }
}