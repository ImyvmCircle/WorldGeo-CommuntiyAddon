package com.imyvm.community.application.interaction.screen

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityListFilterType
import com.imyvm.community.domain.community.CommunityRole
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.inter.screen.CommunityCreationMenu
import com.imyvm.community.inter.screen.CommunityListMenu
import com.imyvm.community.inter.screen.MyCommunityListMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.ImyvmWorldGeo
import net.minecraft.server.network.ServerPlayerEntity

fun runList(player: ServerPlayerEntity) {
    val mode = CommunityListFilterType.JOIN_ABLE
    CommunityMenuOpener.open(player, CommunityListFilterType.JOIN_ABLE) { syncId, _ ->
        CommunityListMenu(syncId, mode)
    }
}

fun runCreate(player: ServerPlayerEntity){
    if (!checkPointSelectingCreating(player)) return

    CommunityMenuOpener.open(player, null) { syncId, _ ->
        CommunityCreationMenu(syncId)
    }
}

fun runMyCommunity(player: ServerPlayerEntity) {
    val joinedCommunities = CommunityDatabase.communities.filter {
        it.member.any { m -> m.key == player.uuid && m.value != CommunityRole.APPLICANT }
    }

    when {
        joinedCommunities.isEmpty() -> {
            player.sendMessage(Translator.tr("ui.main.message.no_community"))
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

private fun checkPointSelectingCreating(player: ServerPlayerEntity): Boolean {
    return if (!ImyvmWorldGeo.pointSelectingPlayers.containsKey(player.uuid)
        || ImyvmWorldGeo.pointSelectingPlayers[player.uuid]?. size!! < 2) {
        player.closeHandledScreen()
        player.sendMessage(Translator.tr("ui.main.create.error.no_selection"))
        false
    } else {
        true
    }
}



