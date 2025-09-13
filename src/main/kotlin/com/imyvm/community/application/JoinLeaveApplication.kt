package com.imyvm.community.application

import com.imyvm.community.CommunityConfig
import com.imyvm.community.CommunityDatabase.Companion.communities
import com.imyvm.community.util.Translator
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityRole
import com.imyvm.community.domain.CommunityStatus
import net.minecraft.server.network.ServerPlayerEntity

fun checkPlayerMembership(player: ServerPlayerEntity, community: Community): Boolean {
    if (isJoinedTarget(player, community)) return false
    if (isJoinedRealmTargetingRealm(player, community)) return false
    if (isJoinedManorTargetingManor(player, community)) return false
    return true
}

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

private fun isJoinedTarget(player: ServerPlayerEntity, targetCommunity: Community): Boolean {
    if (targetCommunity.member.containsKey(player.uuid)){
        return if (targetCommunity.member[player.uuid] == CommunityRole.APPLICANT) {
            player.sendMessage(Translator.tr("community.join.error.already_applied", targetCommunity.id))
            true
        } else {
            player.sendMessage(Translator.tr("community.join.error.already_member", targetCommunity.id))
            true
        }
    }
    return false
}

private fun isJoinedRealmTargetingRealm(player: ServerPlayerEntity, targetCommunity: Community): Boolean {
    if (targetCommunity.status == CommunityStatus.RECRUITING_REALM || targetCommunity.status == CommunityStatus.PENDING_REALM || targetCommunity.status == CommunityStatus.ACTIVE_REALM) {
        val joinedCommunity = communities.find {
            (it.status == CommunityStatus.ACTIVE_REALM || it.status == CommunityStatus.PENDING_REALM || it.status == CommunityStatus.RECRUITING_REALM)
                    && it.member.containsKey(player.uuid)
        }
        if (joinedCommunity != null) {
            player.sendMessage(Translator.tr("community.join.error.already_in_realm", joinedCommunity.id))
            return true
        }
    }
    return false
}

private fun isJoinedManorTargetingManor(player: ServerPlayerEntity, targetCommunity: Community): Boolean {
    if (targetCommunity.status == CommunityStatus.ACTIVE_MANOR || targetCommunity.status == CommunityStatus.PENDING_MANOR) {
        val joinedCommunity = communities.find {
            (it.status == CommunityStatus.ACTIVE_MANOR || it.status == CommunityStatus.PENDING_MANOR)
                    && it.member.containsKey(player.uuid)
        }
        if (joinedCommunity != null) {
            player.sendMessage(Translator.tr("community.join.error.already_in_manor", joinedCommunity.id))
            return true
        }
    }
    return false
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