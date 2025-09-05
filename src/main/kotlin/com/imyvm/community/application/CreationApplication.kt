package com.imyvm.community.application

import com.imyvm.community.CommunityConfig
import com.imyvm.community.Translator
import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.domain.*
import com.imyvm.economy.EconomyMod
import com.imyvm.iwg.ImyvmWorldGeo
import net.minecraft.server.network.ServerPlayerEntity

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

fun initialApplication(player: ServerPlayerEntity, name: String) {
    val community = Community(
        id = 0,
        regionNumberId = ImyvmWorldGeo.data.getRegionList().lastOrNull()?.numberID,
        foundingTimeSeconds = System.currentTimeMillis() / 1000,
        member = hashMapOf(player.uuid to CommunityRole.OWNER),
        joinPolicy = CommunityJoinPolicy.OPEN,
        status = CommunityStatus.PENDING_MANOR
    )
    WorldGeoCommunityAddon.communityData.addCommunity(community)
    player.sendMessage(
        Translator.tr("community.create.request.initial.success", name, community.id)
    )
}

fun handleApplicationBranches(player: ServerPlayerEntity, communityType: String) {
    if (communityType == "manor") {
        player.sendMessage(Translator.tr("community.create.request.sent"))
    } else if (communityType == "realm") {
        player.sendMessage(Translator.tr("community.create.request.recruitment", CommunityConfig.MIN_NUMBER_MEMBER_REALM.value))
        WorldGeoCommunityAddon.pendingOperations[player.uuid] = com.imyvm.community.domain.PendingOperation(
            expireAt = System.currentTimeMillis() + CommunityConfig.APPLICATION_EXPIRE_HOURS.value * 3600 * 1000,
            type = PendingOperationType.CREATE_COMMUNITY_RECRUITMENT
        )
    }
}