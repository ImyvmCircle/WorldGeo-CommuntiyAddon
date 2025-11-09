package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityRoleType
import com.imyvm.community.util.Translator
import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity

fun runOpenPlayerRegionalSettings(community: Community, playerObject: GameProfile, playerExecutor: ServerPlayerEntity) {
    CommunityMenuOpener.open(playerExecutor) { syncId ->  PlayerRegionalSettingMenu(syncId, community, playerObject) }
}

fun runRemoveMember() {

}

fun runNotifyMember() {

}

fun runPromoteMember(
    community: Community,
    playerExecutor: ServerPlayerEntity,
    playerObject: GameProfile,
    governorship: Int = -1,
    isPromote: Boolean = true
) {
    if (isPromote){
        if (governorship == -1) {
            if (community.getMemberRole(playerObject.id) == CommunityRoleType.MEMBER) {
                val memberValue = community.member[playerObject.id]
                if (memberValue != null) {
                    memberValue.basicRoleType = CommunityRoleType.ADMIN
                    playerExecutor.closeHandledScreen()
                    playerExecutor.sendMessage(Translator.tr("community.operation.member.promote.promote.success", playerObject.name))
                } else {
                    playerExecutor.closeHandledScreen()
                    playerExecutor.sendMessage(Translator.tr("community.operation.member.promote.promote.fail.not_member", playerObject.name))
                }
                return
            } else {
                playerExecutor.closeHandledScreen()
                playerExecutor.sendMessage(Translator.tr("community.operation.member.promote.promote.fail.not_member", playerObject.name))
                return
            }
        } else {
            val memberValue = community.member[playerObject.id]
            if (memberValue != null) {
                memberValue.governorship = governorship
                playerExecutor.closeHandledScreen()
                playerExecutor.sendMessage(Translator.tr("community.operation.member.promote.governorship.success", playerObject.name, governorship))
            } else {
                playerExecutor.closeHandledScreen()
                playerExecutor.sendMessage(Translator.tr("community.operation.member.promote.governorship.fail.not_member", playerObject.name))
            }
        }
    } else {
        if (community.getMemberRole(playerObject.id) == CommunityRoleType.ADMIN) {
            val memberValue = community.member[playerObject.id]
            if (memberValue != null) {
                memberValue.basicRoleType = CommunityRoleType.MEMBER
                playerExecutor.closeHandledScreen()
                playerExecutor.sendMessage(Translator.tr("community.operation.member.promote.demote.success", playerObject.name))
            } else {
                playerExecutor.closeHandledScreen()
                playerExecutor.sendMessage(Translator.tr("community.operation.member.promote.demote.fail.not_member", playerObject.name))
            }
            return
        } else {
            playerExecutor.closeHandledScreen()
            playerExecutor.sendMessage(Translator.tr("community.operation.member.promote.demote.fail.not_admin", playerObject.name))
            return
        }
    }
}