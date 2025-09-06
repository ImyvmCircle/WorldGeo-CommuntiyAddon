package com.imyvm.community.application

import CommunityDatabase.Companion.communities
import com.imyvm.community.CommunityConfig
import com.imyvm.community.Translator
import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.WorldGeoCommunityAddon.Companion.logger
import com.imyvm.community.domain.*
import com.imyvm.economy.EconomyMod
import com.imyvm.iwg.ImyvmWorldGeo
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import java.util.UUID

fun chargeFromApplicator(player: ServerPlayerEntity, communityType: String): Int {
    val accountThreshold = when (communityType.lowercase()) {
        "manor" -> CommunityConfig.PRICE_MANOR.value
        "realm" -> CommunityConfig.PRICE_REALM.value
        else -> return 0
    }
    val playerAccount = EconomyMod.data.getOrCreate(player)
    return if (playerAccount.money >= accountThreshold){
        playerAccount.addMoney((-accountThreshold))
        player.sendMessage(Translator.tr("community.create.money.checked", accountThreshold))
        1
    } else {
        player.sendMessage(Translator.tr("community.create.money.error", accountThreshold))
        0
    }
}

fun initialApplication(player: ServerPlayerEntity, name: String, communityType: String) {
    val community = Community(
        id = 0,
        regionNumberId = ImyvmWorldGeo.data.getRegionList().lastOrNull()?.numberID,
        foundingTimeSeconds = System.currentTimeMillis() / 1000,
        member = hashMapOf(player.uuid to CommunityRole.OWNER),
        joinPolicy = CommunityJoinPolicy.OPEN,
        status = if (communityType.equals("manor", ignoreCase = true)) {
            CommunityStatus.PENDING_MANOR
        } else {
            CommunityStatus.RECRUITING_REALM
        }

    )
    WorldGeoCommunityAddon.communityData.addCommunity(community)
    player.sendMessage(
        Translator.tr("community.create.request.initial.success", name, community.id)
    )
}

fun handleApplicationBranches(player: ServerPlayerEntity, communityType: String) {
    val uuid = player.uuid
    if (communityType == "manor") {
        player.sendMessage(Translator.tr("community.create.request.sent"))
        WorldGeoCommunityAddon.pendingOperations[uuid] = PendingOperation(
            expireAt = System.currentTimeMillis() + CommunityConfig.AUDITING_EXPIRE_HOURS.value * 3600 * 1000,
            type = PendingOperationType.AUDITING_COMMUNITY_APPLICATION
        )
    } else if (communityType == "realm") {
        player.sendMessage(Translator.tr("community.create.request.recruitment", CommunityConfig.MIN_NUMBER_MEMBER_REALM.value))
        WorldGeoCommunityAddon.pendingOperations[uuid] = PendingOperation(
            expireAt = System.currentTimeMillis() + CommunityConfig.APPLICATION_EXPIRE_HOURS.value * 3600 * 1000,
            type = PendingOperationType.CREATE_COMMUNITY_RECRUITMENT
        )
    }
}

fun checkMemberNumber(uuid: UUID, iterator:  MutableIterator<MutableMap.MutableEntry<UUID, PendingOperation>>) {
    for (community in communities) {
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

private fun addAuditingApplicationRealm(uuid: UUID, community: Community) {
    WorldGeoCommunityAddon.pendingOperations[uuid] = PendingOperation(
        expireAt = System.currentTimeMillis() + CommunityConfig.AUDITING_EXPIRE_HOURS.value * 3600 * 1000,
        type = PendingOperationType.AUDITING_COMMUNITY_APPLICATION
    )
    community.status = CommunityStatus.PENDING_REALM
    logger.info("Community application from player $uuid moved to auditing stage.")
}

fun removeExpiredApplication(uuid: UUID, server: MinecraftServer) {
    for (community in communities) {
        for (member in community.member) {
            if (member.value == com.imyvm.community.domain.CommunityRole.OWNER && member.key == uuid) {
                community.status = when(community.status) {
                    CommunityStatus.RECRUITING_REALM -> CommunityStatus.REVOKED_REALM
                    else -> community.status
                }
                logger.info("Community ${community.id} recruitment expired and revoked.")

                val player = server.playerManager?.getPlayer(uuid) ?: return
                refundNotCreated(player, community)
            }
        }
    }
}

fun checkPendingPreAuditing(player: ServerPlayerEntity, targetCommunity: Community): Boolean{
    val uuidOwner = targetCommunity.member.filter { it.value == com.imyvm.community.domain.CommunityRole.OWNER }.keys.firstOrNull()
        ?: run {
            player.sendMessage(Translator.tr("community.audit.error.no_owner", targetCommunity.id))
            return false
        }
    if (WorldGeoCommunityAddon.pendingOperations[uuidOwner] == null) {
        player.sendMessage(Translator.tr("community.audit.error.no_pending", targetCommunity.id))
        return false
    }
    WorldGeoCommunityAddon.pendingOperations.remove(uuidOwner)
    return true
}

fun handleAuditingChoices(player: ServerPlayerEntity, choice: String, targetCommunity: Community): Int{
    when (choice) {
        "yes" -> {
            when (targetCommunity.status) {
                CommunityStatus.PENDING_MANOR -> {
                    promoteToActiveManor(player, targetCommunity)
                }
                CommunityStatus.PENDING_REALM -> {
                    promoteToActiveRealm(player, targetCommunity)
                }
                else -> {
                    player.sendMessage(Translator.tr("community.audit.error.invalid_status", targetCommunity.id))
                    return 0
                }
            }
            player.sendMessage(Translator.tr("community.audit.approved", targetCommunity.id))
            return 1
        }
        "no" -> {
            revokeCommunity(player, targetCommunity)
            player.sendMessage(Translator.tr("community.audit.denied", targetCommunity.id))
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
    player.sendMessage(Translator.tr("community.audit.manor.activated", targetCommunity.id))
    logger.info("Community ${targetCommunity.id} promoted to ACTIVE_MANOR by player ${player.uuid}.")
}

private fun promoteToActiveRealm(player: ServerPlayerEntity, targetCommunity: Community) {
    targetCommunity.status = CommunityStatus.ACTIVE_REALM
    player.sendMessage(Translator.tr("community.audit.realm.activated", targetCommunity.id))
    logger.info("Community ${targetCommunity.id} promoted to ACTIVE_REALM by player ${player.uuid}.")
}

private fun revokeCommunity(player: ServerPlayerEntity, targetCommunity: Community) {
    targetCommunity.status = when (targetCommunity.status) {
        CommunityStatus.PENDING_MANOR, CommunityStatus.ACTIVE_MANOR -> CommunityStatus.REVOKED_MANOR
        CommunityStatus.PENDING_REALM, CommunityStatus.RECRUITING_REALM, CommunityStatus.ACTIVE_REALM -> CommunityStatus.REVOKED_REALM
        else -> targetCommunity.status
    }
    refundNotCreated(player, targetCommunity)
    logger.info("Community ${targetCommunity.id} revoked by player ${player.uuid}.")
}

private fun refundNotCreated(player: ServerPlayerEntity, community: Community) {
    val price = when (community.status) {
        CommunityStatus.REVOKED_REALM -> CommunityConfig.PRICE_REALM.value
        else -> CommunityConfig.PRICE_MANOR.value
    }
    val playerAccount = EconomyMod.data.getOrCreate(player)
    playerAccount.addMoney(price)
    logger.info("Refunded $price to player ${player.uuid} for community ${community.id} expiration.")
}