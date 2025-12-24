package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItemStack
import com.imyvm.community.inter.screen.component.getPlayerHeadButtonItemStackCommunity
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunitySettingMenu(
    syncId: Int,
    playerExecutor: ServerPlayerEntity,
    community: Community
) : AbstractMenu(
    syncId,
    menuTitle = generateCommunitySettingMenuTitle(community)
) {
    init {
        addButton(
            slot = 10,
            name = community.generateCommunityMark(),
            itemStack = getPlayerHeadButtonItemStackCommunity(community)
        ) {}

        addButton(
            slot = 19,
            name = Translator.tr("ui.community.setting.button.regional")?.string ?: "Regional Settings",
            item = Items.GRASS_BLOCK
        ) {}

        addButton(
            slot = 21,
            name = Translator.tr("ui.community.setting.button.player")?.string ?: "Player-targeted Settings",
            itemStack = createPlayerHeadItemStack(playerExecutor.name.string, playerExecutor.uuid)
        ) {}
    }

    companion object {
        fun generateCommunitySettingMenuTitle(community: Community): Text {
            return Translator.tr("ui.community.setting.title.full", community.generateCommunityMark())
                ?: Text.of("Community Settings")
        }
    }
}