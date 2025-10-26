package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.CommunityOperationMemberListMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runOPRenameCommunity(player: ServerPlayerEntity, community: Community){
    CommunityMenuOpener.openCommunityRenameAnvilMenu(player, community)
}

fun runOpManageMembers(player: ServerPlayerEntity, community: Community){
    CommunityMenuOpener.open(player, null) { syncId, _ ->
        CommunityOperationMemberListMenu(syncId, community, player)
    }
}