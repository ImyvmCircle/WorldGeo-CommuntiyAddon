package com.imyvm.community.application.interaction.command

import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.application.helper.refundNotCreated
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityStatus
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.util.Translator
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import com.imyvm.iwg.inter.api.RegionDataApi
import net.minecraft.server.network.ServerPlayerEntity

fun onForceDeleteCommunity(player: ServerPlayerEntity, targetCommunity: Community): Int {
    WorldGeoCommunityAddon.pendingOperations.remove(targetCommunity.regionNumberId)

    val region = targetCommunity.regionNumberId?.let { RegionDataApi.getRegion(it) }
    if (region != null) {
        PlayerInteractionApi.deleteRegion(player, region)
    }

    CommunityDatabase.removeCommunity(targetCommunity)


    if (region != null) {
        player.sendMessage(Translator.tr("community.delete.success",
            region.name,
            targetCommunity.regionNumberId))
    } else {
        player.sendMessage(Translator.tr("community.delete.success.null_region"))
    }

    return 1
}

fun onAudit(player: ServerPlayerEntity, choice: String, targetCommunity: Community): Int {
    if (!checkPendingPreAuditing(player, targetCommunity)) return 0
    return handleAuditingChoices(player, choice, targetCommunity)
}


fun onForceRevoke(player: ServerPlayerEntity, targetCommunity: Community): Int {
    revokeCommunity(targetCommunity)
    player.sendMessage(Translator.tr("community.revoke.success", targetCommunity.regionNumberId))
    return 1
}

fun onForceActive(player: ServerPlayerEntity, targetCommunity: Community): Int {
    when (targetCommunity.status) {
        CommunityStatus.REVOKED_MANOR -> promoteToActiveManor(player, targetCommunity)
        CommunityStatus.REVOKED_REALM -> promoteToActiveRealm(player, targetCommunity)
        else -> {
            player.sendMessage(Translator.tr("community.force_active.error_invalid_status", targetCommunity.regionNumberId))
            return 0
        }
    }
    player.sendMessage(Translator.tr("community.force_active.success", targetCommunity.regionNumberId))
    return 1
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
            revokeCommunityWithRefund(player, targetCommunity)
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

private fun revokeCommunityWithRefund(player: ServerPlayerEntity, targetCommunity: Community) {
    revokeCommunity(targetCommunity)
    refundNotCreated(player, targetCommunity)
}

private fun revokeCommunity(targetCommunity: Community) {
    targetCommunity.status = when (targetCommunity.status) {
        CommunityStatus.PENDING_MANOR, CommunityStatus.ACTIVE_MANOR -> CommunityStatus.REVOKED_MANOR
        CommunityStatus.PENDING_REALM, CommunityStatus.RECRUITING_REALM, CommunityStatus.ACTIVE_REALM -> CommunityStatus.REVOKED_REALM
        else -> targetCommunity.status
    }
}