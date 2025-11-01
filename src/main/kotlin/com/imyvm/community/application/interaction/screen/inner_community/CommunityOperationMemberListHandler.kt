package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.CommunityOperationMemberMenu
import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

fun runCommunityOperationMember(community: Community, playerObjectUuid: UUID, playerExecutor: ServerPlayerEntity){
    val server = playerExecutor.getServer()!!
    val playerObject = server.playerManager.getPlayer(playerObjectUuid)
    if (playerObject != null) {
        CommunityMenuOpener.open(playerExecutor) { syncId -> CommunityOperationMemberMenu(syncId, community, playerObject, playerExecutor) }
    } else {
        playerExecutor.closeHandledScreen()
        playerExecutor.sendMessage(Translator.tr("ui.community.member_list.error.offline_member"))
    }
}