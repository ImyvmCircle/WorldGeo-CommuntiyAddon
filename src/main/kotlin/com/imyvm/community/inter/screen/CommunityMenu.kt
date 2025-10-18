package com.imyvm.community.inter.screen

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import com.imyvm.iwg.inter.api.RegionDataApi
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

        addButton(
            slot = 10,
            name = generateCommunityMark(community),
            itemStack = createPlayerHeadItem(generateCommunityMark(community), player.uuid)
        ) {}

        addButton(
            slot = 12,
            name = Translator.tr("ui.community.description.region")?.string ?: "Description",
            item = Items.BOOKSHELF
        ) {
            community.sendCommunityRegionDescription(player)
            it.closeHandledScreen()
        }
    }

    private fun generateCommunityMark(community: Community): String {
        return RegionDataApi.getRegion(community.regionNumberId!!)?.name ?: "Community #${community.regionNumberId}"
    }
}

