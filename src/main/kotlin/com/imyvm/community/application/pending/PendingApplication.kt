package com.imyvm.community.application.pending

import com.imyvm.community.infra.CommunityConfig
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.application.helper.refundNotCreated
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.community.domain.PendingOperation
import com.imyvm.community.domain.PendingOperationType
import net.minecraft.server.MinecraftServer
import java.util.*

fun checkMemberNumber(uuid: UUID, iterator:  MutableIterator<MutableMap.MutableEntry<UUID, PendingOperation>>) {
    for (community in CommunityDatabase.communities) {
        for (member in community.member) {
            if (member.value == com.imyvm.community.domain.CommunityRole.OWNER && member.key == uuid) {
                if (community.member
                        .count { it.value != com.imyvm.community.domain.CommunityRole.APPLICANT } >= CommunityConfig.MIN_NUMBER_MEMBER_REALM.value
                    && community.status == CommunityStatus.PENDING_REALM) {
                    iterator.remove()
                    addAuditingApplicationRealm(uuid, community)
                }
            }
        }
    }
}

fun removeExpiredApplication(uuid: UUID, server: MinecraftServer) {
    for (community in CommunityDatabase.communities) {
        for (member in community.member) {
            if (member.value == com.imyvm.community.domain.CommunityRole.OWNER && member.key == uuid) {
                community.status = when(community.status) {
                    CommunityStatus.RECRUITING_REALM -> CommunityStatus.REVOKED_REALM
                    else -> community.status
                }
                WorldGeoCommunityAddon.logger.info("Community ${community.regionNumberId} recruitment expired and revoked.")

                val player = server.playerManager?.getPlayer(uuid) ?: return
                refundNotCreated(player, community)
            }
        }
    }
}

private fun addAuditingApplicationRealm(uuid: UUID, community: Community) {
    WorldGeoCommunityAddon.pendingOperations[uuid] = PendingOperation(
        expireAt = System.currentTimeMillis() + CommunityConfig.AUDITING_EXPIRE_HOURS.value * 3600 * 1000,
        type = PendingOperationType.AUDITING_COMMUNITY_APPLICATION
    )
    community.status = CommunityStatus.PENDING_REALM
    WorldGeoCommunityAddon.logger.info("Community application from player $uuid moved to auditing stage.")
}