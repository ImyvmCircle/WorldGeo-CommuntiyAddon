package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.CommunityOperationMemberMenu
import com.imyvm.community.util.Translator
import com.mojang.authlib.GameProfile
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

fun runCommunityOperationMember(community: Community, playerObjectUuid: UUID, playerExecutor: ServerPlayerEntity){
    val server = playerExecutor.getServer()!!
    val playerObjectProfile = getPlayerProfileByUuid(playerObjectUuid, server)
    if (playerObjectProfile != null) {
        CommunityMenuOpener.open(playerExecutor) { syncId -> CommunityOperationMemberMenu(syncId, community, playerObjectProfile, playerExecutor) }
    } else {
        playerExecutor.closeHandledScreen()
        playerExecutor.sendMessage(Translator.tr("ui.community.member_list.error.offline_member"))
    }
}

@Deprecated(
    message = "Use resolvePlayerName from imyvm-iwg util package",
    replaceWith = ReplaceWith("UtilApi.getPlayerProfile(uuid, player)")
)
private fun getPlayerProfileByUuid(uuid: UUID, server: MinecraftServer): GameProfile? {
    return server.userCache?.getByUuid(uuid)?.orElse(null)
}