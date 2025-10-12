package com.imyvm.community.inter.screen

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.util.Translator
import net.minecraft.item.Items

class MyCommunityListMenu(
    syncId: Int,
    private val joinedCommunities: List<Community>
) : AbstractMenu(
    syncId,
    menuTitle = Translator.tr("ui.my_communities.title")
) {
    init {
        addCommunityButtons()
    }

    private fun addCommunityButtons() {
        var slot = 10
        for (community in joinedCommunities.take(26)) {
            val name = community.getRegion()?.name ?: "Community #${community.regionNumberId}"

            addButton(slot, name, Items.PAPER) { player ->
                CommunityMenuOpener.openCommunityMenu(player, community)
            }

            slot++
            if (slot > 35) break
        }
    }
}

