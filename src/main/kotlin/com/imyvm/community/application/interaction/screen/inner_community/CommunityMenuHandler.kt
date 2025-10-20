package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.OperationMenu
import net.minecraft.server.network.ServerPlayerEntity

fun runOpenOperationMenu(player: ServerPlayerEntity, community: Community) {
    CommunityMenuOpener.open(player, null) { syncId, _ ->
        OperationMenu(syncId, community, player)
    }
}