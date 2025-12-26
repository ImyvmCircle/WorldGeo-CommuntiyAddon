package com.imyvm.community.application.interaction.screen.outer_community

import com.imyvm.community.application.interaction.common.helper.checkPlayerMembershipPreCreation
import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityListFilterType
import com.imyvm.community.domain.community.MemberRoleType
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.inter.screen.outer_community.CommunityCreationMenu
import com.imyvm.community.inter.screen.outer_community.CommunityListMenu
import com.imyvm.community.inter.screen.outer_community.MainMenu
import com.imyvm.community.inter.screen.outer_community.MyCommunityListMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.ImyvmWorldGeo
import net.minecraft.server.network.ServerPlayerEntity

fun runList(player: ServerPlayerEntity) {
    val mode = CommunityListFilterType.JOIN_ABLE
    CommunityMenuOpener.open(player) { syncId -> CommunityListMenu(syncId, mode)
    }
}

fun runCreate(player: ServerPlayerEntity){
    if (!checkPointSelectingCreating(player)) return
    if (!checkPlayerMembershipPreCreation(player)) return
    val defaultTitle = generateNewCommunityTitle()
    CommunityMenuOpener.open(player) { syncId ->
        CommunityCreationMenu(
            syncId,
            currentName = defaultTitle,
            playerEntity = player,
            runBack = { runBackMainMenu(it) }
        )
    }
}

fun runMyCommunity(player: ServerPlayerEntity) {
    val joinedCommunities = CommunityDatabase.communities.filter {
        it.member.any { m -> m.key == player.uuid &&
                it.getMemberRole(m.key) != MemberRoleType.APPLICANT &&
                it.getMemberRole(m.key) != MemberRoleType.REFUSED }
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
            CommunityMenuOpener.open(player) { syncId -> MyCommunityListMenu(syncId, content)
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

private fun generateNewCommunityTitle(): String {
    val index = CommunityDatabase.communities.size + 1
    val defaultTitle = Translator.tr("ui.create.title", index)?.string ?: "New-Community"
    return generateSequence(index) { it + 1 }
        .map { "$defaultTitle$it" }
        .first { title -> CommunityDatabase.communities.none { it.getRegion()?.name == title } }
}

private fun runBackMainMenu(player: ServerPlayerEntity) {
    CommunityMenuOpener.open(player) { syncId ->
        MainMenu(syncId)
    }
}
