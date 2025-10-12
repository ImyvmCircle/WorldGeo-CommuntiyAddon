package com.imyvm.community.application.interaction.screen

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityRole
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.inter.screen.CommunityListMenu
import com.imyvm.community.inter.screen.MyCommunityListMenu
import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

fun runList(player: ServerPlayerEntity) {
    CommunityMenuOpener.open(player, "JOIN-ABLE") { syncId, mode ->
        CommunityListMenu(syncId, mode ?: "JOIN-ABLE")
    }
}

fun runCreate(player: ServerPlayerEntity){
    player.sendMessage(Text.literal("创建聚落（未实现）"))
}

fun runMyCommunity(player: ServerPlayerEntity) {
    val joinedCommunities = CommunityDatabase.communities.filter {
        it.member.any { m -> m.key == player.uuid && m.value != CommunityRole.APPLICANT }
    }

    when {
        joinedCommunities.isEmpty() -> {
            player.sendMessage(
                Translator.tr("ui.main.message.no_community")
            )
            player.closeHandledScreen()
        }

        joinedCommunities.size == 1 -> {
            val community = joinedCommunities.first()
            CommunityMenuOpener.openCommunityMenu(player, community)
        }

        else -> {
            val content: List<Community> = joinedCommunities.toList()
            CommunityMenuOpener.open(player, content) { syncId, c ->
                MyCommunityListMenu(syncId, c!!)
            }
        }
    }
}



