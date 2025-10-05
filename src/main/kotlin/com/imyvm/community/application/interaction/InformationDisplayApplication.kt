package com.imyvm.community.application.interaction

import com.imyvm.community.infra.CommunityDatabase.Companion.communities
import com.imyvm.community.util.Translator
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.iwg.domain.Region
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import net.minecraft.server.network.ServerPlayerEntity

fun onListCommunities(player: ServerPlayerEntity, type: String) : Int {
    when(type){
        "RECRUITING" -> listRecruitingCommunities(player)
        "AUDITING" -> listPendingAuditingCommunities(player)
        "ACTIVE" -> listActiveCommunities(player)
        "ALL" -> listAllCommunities(player)
    }
    return 1
}

fun runQueryCommunityRegion(player: ServerPlayerEntity, region: Region): Int {
    PlayerInteractionApi.queryRegionInfo(player, region)
    return 1
}

private fun listRecruitingCommunities(player: ServerPlayerEntity): Int{
    player.sendMessage(Translator.tr("community.list.header.recruiting"))
    for (community in communities) {
        if (community.status == CommunityStatus.RECRUITING_REALM) {
            displayCommunityEntry(player, community)
        }
    }
    return 1
}

private fun listPendingAuditingCommunities(player: ServerPlayerEntity): Int{
    player.sendMessage(Translator.tr("community.list.header.pending"))
    for (community in communities) {
        if (community.status == CommunityStatus.PENDING_MANOR
            || community.status == CommunityStatus.PENDING_REALM) {
            displayCommunityEntry(player, community)
        }
    }
    return 1
}

private fun listActiveCommunities(player: ServerPlayerEntity): Int{
    player.sendMessage(Translator.tr("community.list.header.active"))
    for (community in communities) {
        if (community.status == CommunityStatus.ACTIVE_MANOR
            || community.status == CommunityStatus.ACTIVE_REALM) {
            displayCommunityEntry(player, community)
        }
    }
    return 1
}

private fun listAllCommunities(player: ServerPlayerEntity): Int{
    player.sendMessage(Translator.tr("community.list.header.all"))
    for (community in communities) {
        displayCommunityEntry(player, community)
    }
    return 1
}

private fun displayCommunityEntry(player: ServerPlayerEntity, community: Community){
    player.sendMessage(
        community.getRegion()?.let {
            Translator.tr(
                "community.list.entry",
                it.name,
                community.regionNumberId,
                community.foundingTimeSeconds,
                community.status.name.lowercase(),
                community.joinPolicy.name.lowercase(),
                community.member.size
            )
        }
    )
}