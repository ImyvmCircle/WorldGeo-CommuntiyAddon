package com.imyvm.community.inter.screen

import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity

class CommunityMenu(
    syncId: Int,
    content: Pair<ServerPlayerEntity, Int>
) : AbstractMenu(
    syncId,
    menuTitle = Translator.tr("ui.community.title")
) {
    init {
        val (player, regionId) = content
        loadCommunityUI(regionId)
    }

    private fun loadCommunityUI( regionId: Int) {
        addButton(10, "Community #$regionId", Items.PAPER) {}
    }
}

