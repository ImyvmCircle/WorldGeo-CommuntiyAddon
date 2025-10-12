package com.imyvm.community.application.interaction.screen

import com.imyvm.community.inter.screen.CommunityListMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runSwitchMode(player: ServerPlayerEntity, mode: String = "ALL") {
    CommunityMenuOpener.open(player, mode) { syncId, _ ->
        CommunityListMenu(syncId, mode)
    }
}