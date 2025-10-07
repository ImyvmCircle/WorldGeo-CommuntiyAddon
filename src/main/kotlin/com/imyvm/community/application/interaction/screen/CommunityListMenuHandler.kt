package com.imyvm.community.application.interaction.screen

import com.imyvm.community.inter.screen.CommunityListMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runSwitchMode(player: ServerPlayerEntity, cont: Any?){
    when (cont as? String) {
        "ALL" -> CommunityMenuOpener.open(player, "ALL") { syncId, content -> CommunityListMenu(syncId, content) }
        "RECRUITING" -> CommunityMenuOpener.open(player, "RECRUITING") { syncId, content -> CommunityListMenu(syncId, content) }
        "AUDITING" -> CommunityMenuOpener.open(player, "AUDITING") { syncId, content -> CommunityListMenu(syncId, content) }
        "ACTIVE" -> CommunityMenuOpener.open(player, "ACTIVE") { syncId, content -> CommunityListMenu(syncId, content) }
        else -> CommunityMenuOpener.open(player, "ALL") { syncId, content -> CommunityListMenu(syncId, content) }
    }
}