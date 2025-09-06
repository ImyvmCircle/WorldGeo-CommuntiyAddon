package com.imyvm.community.application

import com.imyvm.community.CommunityConfig
import com.imyvm.community.Translator
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityStatus
import net.minecraft.server.network.ServerPlayerEntity

fun checkMemberNumberManor(player: ServerPlayerEntity,targetCommunity: Community): Boolean {
    if (CommunityConfig.IS_CHECKING_MANOR_MEMBER_SIZE.value) {
        if ((targetCommunity.status == CommunityStatus.ACTIVE_MANOR  || targetCommunity.status == CommunityStatus.PENDING_MANOR) &&
            targetCommunity.member.count { it.value != com.imyvm.community.domain.CommunityRole.APPLICANT } >= CommunityConfig.MIN_NUMBER_MEMBER_REALM.value) {
            player.sendMessage(Translator.tr("community.join.error.full", CommunityConfig.MIN_NUMBER_MEMBER_REALM.value))
            return false
        }
    }

    return true
}

fun tryJoinByPolicy(player: ServerPlayerEntity, targetCommunity: Community): Int {
    when (targetCommunity.joinPolicy) {
        com.imyvm.community.domain.CommunityJoinPolicy.OPEN -> return joinUnderOpenPolicy(player, targetCommunity)
        com.imyvm.community.domain.CommunityJoinPolicy.APPLICATION -> return joinUnderApplicationPolicy(player, targetCommunity)
        com.imyvm.community.domain.CommunityJoinPolicy.INVITE_ONLY -> joinUnderInviteOnlyPolicy(player, targetCommunity)
    }

    return 0
}

private fun joinUnderOpenPolicy(player: ServerPlayerEntity, targetCommunity: Community): Int {
    targetCommunity.member[player.uuid] = com.imyvm.community.domain.CommunityRole.MEMBER
    player.sendMessage(Translator.tr("community.join.success", targetCommunity.id))
    return 1
}

private fun joinUnderApplicationPolicy(player: ServerPlayerEntity, targetCommunity: Community): Int {
    if (targetCommunity.member.containsKey(player.uuid)) {
        player.sendMessage(Translator.tr("community.join.error.already_applied", targetCommunity.id))
        return 0
    }
    targetCommunity.member[player.uuid] = com.imyvm.community.domain.CommunityRole.APPLICANT
    player.sendMessage(targetCommunity.getRegion()
        ?.let { Translator.tr("community.join.applied", it.name ,targetCommunity.id) })
    return 1
}

private fun joinUnderInviteOnlyPolicy(player: ServerPlayerEntity, targetCommunity: Community): Int {
    player.sendMessage(Translator.tr("community.join.error.invite_only", targetCommunity.id))
    return 0
}