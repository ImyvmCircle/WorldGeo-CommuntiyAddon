package com.imyvm.community.application

import CommunityDatabase.Companion.communities
import com.imyvm.community.CommunityConfig
import com.imyvm.community.Translator
import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.WorldGeoCommunityAddon.Companion.logger
import com.imyvm.community.domain.*
import com.imyvm.economy.EconomyMod
import com.imyvm.iwg.ImyvmWorldGeo
import net.minecraft.block.entity.VaultBlockEntity.Server
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
                    addAuditingApplication(uuid, community)
                }
            }
        }
    }
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

fun addAuditingApplication(uuid: UUID, community: Community) {
    WorldGeoCommunityAddon.pendingOperations[uuid] = PendingOperation(
        expireAt = System.currentTimeMillis() + CommunityConfig.AUDITING_EXPIRE_HOURS.value * 3600 * 1000,
        type = PendingOperationType.AUDITING_COMMUNITY_APPLICATION
    )
    community.status = CommunityStatus.PENDING_REALM
    logger.info("Community application from player $uuid moved to auditing stage.")
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