package com.imyvm.community.application.interaction.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.operation.CommunityOperationMemberMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.inter.api.UtilApi
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

fun runCommunityOperationMember(community: Community, playerObjectUuid: UUID, playerExecutor: ServerPlayerEntity){
    val playerObjectProfile = UtilApi.getPlayerProfile(playerExecutor, playerObjectUuid)
    if (playerObjectProfile != null) {
        CommunityMenuOpener.open(playerExecutor) { syncId -> CommunityOperationMemberMenu(syncId, community, playerObjectProfile, playerExecutor) }
    } else {
        playerExecutor.closeHandledScreen()
        playerExecutor.sendMessage(Translator.tr("ui.community.member_list.error.offline_member"))
    }
}