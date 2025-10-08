package com.imyvm.community.application.event

import com.imyvm.community.infra.CommunityConfig
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.application.helper.refundNotCreated
import com.imyvm.community.domain.*
import com.imyvm.community.util.Translator
import net.minecraft.server.MinecraftServer
import java.util.*

internal fun checkPendingOperations(server: MinecraftServer) {
    val now = System.currentTimeMillis()
    val iterator: MutableIterator<MutableMap.MutableEntry<UUID, PendingOperation>> =
        WorldGeoCommunityAddon.pendingOperations.iterator()

    while (iterator.hasNext()) {
        val (uuid, operation) = iterator.next()
        if (operation.expireAt <= now) {
            handleExpiredOperation(uuid, operation, iterator, server)
        }
    }
}

private fun handleExpiredOperation(
    uuid: UUID,
    operation: PendingOperation,
    iterator: MutableIterator<MutableMap.MutableEntry<UUID, PendingOperation>>,
    server: MinecraftServer
) {
    when (operation.type) {
        PendingOperationType.CREATE_COMMUNITY_RECRUITMENT -> {
            promoteCommunityIfEligible(uuid)
            removeExpiredApplication(uuid, server)
        }
        else -> {
            WorldGeoCommunityAddon.logger.info(
                "Unhandled expired operation type: ${operation.type} for player $uuid"
            )
        }
    }

    removePendingOperation(uuid, iterator, server, operation.type)
}

private fun promoteCommunityIfEligible(uuid: UUID) {
    for (community in CommunityDatabase.communities) {
        val ownerEntry = community.member.entries.find { it.key == uuid && it.value == CommunityRole.OWNER }
        if (ownerEntry != null &&
            community.member.count { it.value != CommunityRole.APPLICANT } >= CommunityConfig.MIN_NUMBER_MEMBER_REALM.value &&
            community.status == CommunityStatus.PENDING_REALM
        ) {
            addAuditingApplicationRealm(uuid, community)
            WorldGeoCommunityAddon.logger.info("Community ${community.regionNumberId} promoted to auditing stage.")
        }
    }
}

private fun removeExpiredApplication(uuid: UUID, server: MinecraftServer) {
    for (community in CommunityDatabase.communities) {
        val ownerEntry = community.member.entries.find { it.key == uuid && it.value == CommunityRole.OWNER }
        if (ownerEntry != null) {
            community.status = when (community.status) {
                CommunityStatus.RECRUITING_REALM -> CommunityStatus.REVOKED_REALM
                else -> community.status
            }
            WorldGeoCommunityAddon.logger.info("Community ${community.regionNumberId} recruitment expired and revoked.")
            val player = server.playerManager?.getPlayer(uuid) ?: return
            refundNotCreated(player, community)
        }
    }
}

private fun removePendingOperation(
    uuid: UUID,
    iterator: MutableIterator<MutableMap.MutableEntry<UUID, PendingOperation>>,
    server: MinecraftServer,
    operationType: PendingOperationType
) {
    iterator.remove()
    WorldGeoCommunityAddon.logger.info("Removed expired pending operation for player $uuid")
    server.playerManager.getPlayer(uuid)
        ?.sendMessage(Translator.tr("pending.expired", operationType), false)
}

private fun addAuditingApplicationRealm(uuid: UUID, community: Community) {
    WorldGeoCommunityAddon.pendingOperations[uuid] = PendingOperation(
        expireAt = System.currentTimeMillis() + CommunityConfig.AUDITING_EXPIRE_HOURS.value * 3600 * 1000,
        type = PendingOperationType.AUDITING_COMMUNITY_APPLICATION
    )
    community.status = CommunityStatus.PENDING_REALM
    WorldGeoCommunityAddon.logger.info("Community application from player $uuid moved to auditing stage.")
}