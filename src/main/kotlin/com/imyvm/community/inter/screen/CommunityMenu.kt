package com.imyvm.community.inter.screen

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import com.imyvm.iwg.inter.api.RegionDataApi
import net.minecraft.server.network.ServerPlayerEntity

class CommunityMenu(
    syncId: Int,
    content: Pair<ServerPlayerEntity, Community>
) : AbstractMenu(
    syncId,
    menuTitle = content.second.getRegion()?.let { Translator.tr("ui.community.title", it.name , it.numberID)}
) {
    init {
        val (player, community) = content
        addButton(
            slot = 13,
            name = RegionDataApi.getRegion(community.regionNumberId!!)?.name ?: "Community #${community.regionNumberId}",
            itemStack = createPlayerHeadItem(player.name.string, player.uuid)
        ) {}
    }
}

