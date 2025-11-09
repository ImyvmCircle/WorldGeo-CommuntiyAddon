package com.imyvm.community.application.interaction.command

import com.imyvm.community.application.interaction.common.helper.checkPlayerMembershipJoin
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.MemberAccount
import com.imyvm.community.domain.community.CommunityJoinPolicy
import com.imyvm.community.domain.community.CommunityRoleType
import com.imyvm.community.domain.community.CommunityStatus
import com.imyvm.community.infra.CommunityConfig
import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity

fun onJoinCommunity(player: ServerPlayerEntity, targetCommunity: Community): Int {
    if (!checkPlayerMembershipJoin(player, targetCommunity)) return 0
    if (!checkMemberNumberManor(player, targetCommunity)) return 0
    return tryJoinByPolicy(player, targetCommunity)
}

fun checkMemberNumberManor(player: ServerPlayerEntity,targetCommunity: Community): Boolean {
    if (CommunityConfig.IS_CHECKING_MANOR_MEMBER_SIZE.value) {
        if ((targetCommunity.status == CommunityStatus.ACTIVE_MANOR  || targetCommunity.status == CommunityStatus.PENDING_MANOR) &&
            targetCommunity.member.count { targetCommunity.getMemberRole(it.key) != CommunityRoleType.APPLICANT } >= CommunityConfig.MIN_NUMBER_MEMBER_REALM.value) {
            player.sendMessage(Translator.tr("community.join.error.full", CommunityConfig.MIN_NUMBER_MEMBER_REALM.value))
            return false
        }
    }
    return true
}

fun tryJoinByPolicy(player: ServerPlayerEntity, targetCommunity: Community): Int {
    when (targetCommunity.joinPolicy) {
        CommunityJoinPolicy.OPEN -> return joinUnderOpenPolicy(player, targetCommunity)
        CommunityJoinPolicy.APPLICATION -> return joinUnderApplicationPolicy(player, targetCommunity)
        CommunityJoinPolicy.INVITE_ONLY -> joinUnderInviteOnlyPolicy(player, targetCommunity)
    }

    return 0
}

private fun joinUnderOpenPolicy(player: ServerPlayerEntity, targetCommunity: Community): Int {
    targetCommunity.member[player.uuid] = MemberAccount(
        joinedTime = System.currentTimeMillis(),
        basicRoleType = CommunityRoleType.MEMBER
    )
    player.sendMessage(Translator.tr("community.join.success", targetCommunity.regionNumberId))
    return 1
}

private fun joinUnderApplicationPolicy(player: ServerPlayerEntity, targetCommunity: Community): Int {
    if (targetCommunity.member.containsKey(player.uuid)) {
        player.sendMessage(Translator.tr("community.join.error.already_applied", targetCommunity.regionNumberId))
        return 0
    }
    targetCommunity.member[player.uuid] = MemberAccount(
        joinedTime = System.currentTimeMillis(),
        basicRoleType = CommunityRoleType.APPLICANT
    )
    player.sendMessage(targetCommunity.getRegion()
        ?.let { Translator.tr("community.join.applied", it.name ,targetCommunity.regionNumberId) })
    return 1
}

private fun joinUnderInviteOnlyPolicy(player: ServerPlayerEntity, targetCommunity: Community): Int {
    player.sendMessage(Translator.tr("community.join.error.invite_only", targetCommunity.regionNumberId))
    return 0
}