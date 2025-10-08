package com.imyvm.community.application.interaction.command

import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.domain.*
import com.imyvm.community.infra.CommunityConfig
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.util.Translator
import com.imyvm.economy.EconomyMod
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import com.imyvm.iwg.inter.api.RegionDataApi.getRegionList
import net.minecraft.server.network.ServerPlayerEntity

fun onCreateCommunity(player: ServerPlayerEntity, communityType: String, name: String, shapeName: String): Int {
    if (PlayerInteractionApi.createRegion(player, name, shapeName) == 0) {
        player.sendMessage(Translator.tr("community.create.region.error"))
        return 0
    }
    if (chargeFromApplicator(player, communityType) == 0) {
        val regionDelete = getRegionList().lastOrNull()
        if (regionDelete != null) {
            PlayerInteractionApi.deleteRegion(player, regionDelete)
        }
        return 0
    }
    initialApplication(player, name, communityType)
    handleApplicationBranches(player, communityType)
    return 1
}

private fun chargeFromApplicator(player: ServerPlayerEntity, communityType: String): Int {
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

private fun initialApplication(player: ServerPlayerEntity, name: String, communityType: String) {
    val community = Community(
        regionNumberId = getRegionList().lastOrNull()?.numberID,
        foundingTimeSeconds = System.currentTimeMillis() / 1000,
        member = hashMapOf(player.uuid to CommunityRole.OWNER),
        joinPolicy = CommunityJoinPolicy.OPEN,
        status = if (communityType.equals("manor", ignoreCase = true)) {
            CommunityStatus.PENDING_MANOR
        } else {
            CommunityStatus.RECRUITING_REALM
        }

    )
    CommunityDatabase.addCommunity(community)
    player.sendMessage(
        Translator.tr("community.create.request.initial.success", name, community.regionNumberId)
    )
}

private fun handleApplicationBranches(player: ServerPlayerEntity, communityType: String) {
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