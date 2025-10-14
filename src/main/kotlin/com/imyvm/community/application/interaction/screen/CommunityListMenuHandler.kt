package com.imyvm.community.application.interaction.screen

import com.imyvm.community.domain.community.CommunityListFilterType
import com.imyvm.community.inter.screen.CommunityListMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runSwitchFilterMode(player: ServerPlayerEntity, mode: CommunityListFilterType) {
    CommunityMenuOpener.open(player, mode) { syncId, _ ->
        CommunityListMenu(syncId, mode)
    }
}