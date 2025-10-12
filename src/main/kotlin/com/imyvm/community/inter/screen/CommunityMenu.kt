package com.imyvm.community.inter.screen

import com.imyvm.community.domain.Community
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
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
        loadCommunityUI(community)
    }

    private fun loadCommunityUI(community: Community) {
        addButton(10, "Community #${community.getRegion()?.name}", Items.PAPER) {}
    }
}

