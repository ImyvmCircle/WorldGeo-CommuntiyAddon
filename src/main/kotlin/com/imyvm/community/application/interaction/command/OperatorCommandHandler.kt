package com.imyvm.community.application.interaction.command

import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.application.helper.refundNotCreated
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity

fun onAudit(player: ServerPlayerEntity, choice: String, targetCommunity: Community): Int {
    if (!checkPendingPreAuditing(player, targetCommunity)) return 0
    return handleAuditingChoices(player, choice, targetCommunity)
}

private fun checkPendingPreAuditing(player: ServerPlayerEntity, targetCommunity: Community): Boolean {
    val regionId = targetCommunity.regionNumberId
    if (WorldGeoCommunityAddon.pendingOperations[regionId] == null) {
        player.sendMessage(Translator.tr("community.audit.error.no_pending", regionId))
        return false
    }
    WorldGeoCommunityAddon.pendingOperations.remove(regionId)
    return true
}

private fun handleAuditingChoices(player: ServerPlayerEntity, choice: String, targetCommunity: Community): Int {
    when (choice.lowercase()) {
        "yes" -> {
            when (targetCommunity.status) {
                CommunityStatus.PENDING_MANOR -> promoteToActiveManor(player, targetCommunity)
                CommunityStatus.PENDING_REALM -> promoteToActiveRealm(player, targetCommunity)
                else -> {
                    player.sendMessage(Translator.tr("community.audit.error.invalid_status", targetCommunity.regionNumberId))
                    return 0
                }
            }
            player.sendMessage(Translator.tr("community.audit.approved", targetCommunity.regionNumberId))
            return 1
        }
        "no" -> {
            revokeCommunity(player, targetCommunity)
            player.sendMessage(Translator.tr("community.audit.denied", targetCommunity.regionNumberId))
            return 1
        }
        else -> {
            player.sendMessage(Translator.tr("community.audit.error.invalid_choice", choice))
            return 0
        }
    }
}

private fun promoteToActiveManor(player: ServerPlayerEntity, targetCommunity: Community) {
    targetCommunity.status = CommunityStatus.ACTIVE_MANOR
    player.sendMessage(Translator.tr("community.audit.manor.activated", targetCommunity.regionNumberId))
    WorldGeoCommunityAddon.logger.info("Community ${targetCommunity.regionNumberId} promoted to ACTIVE_MANOR by player ${player.uuid}.")
}

private fun promoteToActiveRealm(player: ServerPlayerEntity, targetCommunity: Community) {
    targetCommunity.status = CommunityStatus.ACTIVE_REALM
    player.sendMessage(Translator.tr("community.audit.realm.activated", targetCommunity.regionNumberId))
    WorldGeoCommunityAddon.logger.info("Community ${targetCommunity.regionNumberId} promoted to ACTIVE_REALM by player ${player.uuid}.")
}

private fun revokeCommunity(player: ServerPlayerEntity, targetCommunity: Community) {
    targetCommunity.status = when (targetCommunity.status) {
        CommunityStatus.PENDING_MANOR, CommunityStatus.ACTIVE_MANOR -> CommunityStatus.REVOKED_MANOR
        CommunityStatus.PENDING_REALM, CommunityStatus.RECRUITING_REALM, CommunityStatus.ACTIVE_REALM -> CommunityStatus.REVOKED_REALM
        else -> targetCommunity.status
    }
    refundNotCreated(player, targetCommunity)
    WorldGeoCommunityAddon.logger.info("Community ${targetCommunity.regionNumberId} revoked by player ${player.uuid}.")
}