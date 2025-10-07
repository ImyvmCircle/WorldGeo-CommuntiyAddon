package com.imyvm.community.application.interaction.command

import com.imyvm.community.application.interaction.common.filterCommunitiesByType
import com.imyvm.community.util.Translator
import com.imyvm.community.domain.Community
import com.imyvm.iwg.domain.Region
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import net.minecraft.server.network.ServerPlayerEntity

fun onListCommunities(player: ServerPlayerEntity, type: String): Int {
    val filtered = filterCommunitiesByType(type)
    displayCommunityList(player, type, filtered)
    return 1
}

fun onQueryCommunityRegion(player: ServerPlayerEntity, region: Region): Int {
    PlayerInteractionApi.queryRegionInfo(player, region)
    return 1
}

private fun displayCommunityList(player: ServerPlayerEntity, type: String, list: List<Community>) {
    if (list.isEmpty()) {
        player.sendMessage(Translator.tr("community.list.empty"))
        return
    }
    val headerKey = when (type) {
        "RECRUITING" -> "community.list.header.recruiting"
        "AUDITING" -> "community.list.header.pending"
        "ACTIVE" -> "community.list.header.active"
        "ALL" -> "community.list.header.all"
        else -> "community.list.header.unknown"
    }

    player.sendMessage(Translator.tr(headerKey))
    for (community in list) {
        displayCommunityEntry(player, community)
    }
}

private fun displayCommunityEntry(player: ServerPlayerEntity, community: Community) {
    community.getRegion()?.let {
        player.sendMessage(
            Translator.tr(
                "community.list.entry",
                it.name,
                community.regionNumberId,
                community.foundingTimeSeconds,
                community.status.name.lowercase(),
                community.joinPolicy.name.lowercase(),
                community.member.size
            )
        )
    }
}
