package com.imyvm.community.application.interaction.screen.outer_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.community.CommunityListFilterType
import com.imyvm.community.inter.screen.outer_community.CommunityListMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runSwitchFilterMode(player: ServerPlayerEntity, mode: CommunityListFilterType) {
    CommunityMenuOpener.open(player) { syncId -> CommunityListMenu(syncId, mode)
    }
}