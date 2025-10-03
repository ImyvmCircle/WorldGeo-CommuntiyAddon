package com.imyvm.community.application.helper

import com.imyvm.community.CommunityConfig
import com.imyvm.community.WorldGeoCommunityAddon
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.CommunityStatus
import com.imyvm.economy.EconomyMod
import net.minecraft.server.network.ServerPlayerEntity

fun refundNotCreated(player: ServerPlayerEntity, community: Community) {
    val price = when (community.status) {
        CommunityStatus.REVOKED_REALM -> CommunityConfig.PRICE_REALM.value
        else -> CommunityConfig.PRICE_MANOR.value
    }
    val playerAccount = EconomyMod.data.getOrCreate(player)
    playerAccount.addMoney(price)
    WorldGeoCommunityAddon.logger.info("Refunded $price to player ${player.uuid} for community ${community.regionNumberId} expiration or rejection.")
}