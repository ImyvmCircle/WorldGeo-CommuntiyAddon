package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity

fun runOpenPlayerRegionalSettings(community: Community, playerObject: GameProfile, playerExecutor: ServerPlayerEntity) {
    CommunityMenuOpener.open(playerExecutor) { syncId ->  PlayerRegionalSettingMenu(syncId, community, playerObject) }
}

fun runRemoveMember() {

}

fun runNotifyMember() {

}

fun runPromoteMember() {

}