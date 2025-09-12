package com.imyvm.community.application

import com.imyvm.community.CommunityDatabase.Companion.communities
import com.imyvm.community.Translator
import com.imyvm.community.domain.CommunityStatus
import net.minecraft.server.network.ServerPlayerEntity

fun listPendingAuditingCommunities(player: ServerPlayerEntity): Int{
    player.sendMessage(Translator.tr("community.list.header.pending"))
    for (community in communities) {
        if (community.status == CommunityStatus.PENDING_MANOR
            || community.status == CommunityStatus.PENDING_REALM) {
            player.sendMessage(
                community.getRegion()?.let {
                    Translator.tr(
                        "community.list.entry",
                        it.name,
                        community.id,
                        community.foundingTimeSeconds,
                        community.status.name.lowercase(),
                        community.joinPolicy.name.lowercase(),
                        community.member.size
                    )
                }
            )
        }
    }
    return 1
}

fun listAllCommunities(player: ServerPlayerEntity): Int{
    player.sendMessage(Translator.tr("community.list.header.all"))
    for (community in communities) {
        player.sendMessage(
            community.getRegion()?.let {
                Translator.tr(
                    "community.list.entry",
                    it.name,
                    community.id,
                    community.foundingTimeSeconds,
                    community.status.name.lowercase(),
                    community.joinPolicy.name.lowercase(),
                    community.member.size
                )
            }
        )
    }
    return 1
}