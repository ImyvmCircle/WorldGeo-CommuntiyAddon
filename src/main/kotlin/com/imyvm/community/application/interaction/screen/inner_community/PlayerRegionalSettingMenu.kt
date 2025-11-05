package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.mojang.authlib.GameProfile
import net.minecraft.text.Text

class PlayerRegionalSettingMenu(
    syncId: Int,
    val community: Community,
    private val playerProfile: GameProfile
) : AbstractMenu(
    syncId,
    menuTitle = generatePlayerRegionSettingMenuTitle(community, playerProfile)
) {
    companion object {
        fun generatePlayerRegionSettingMenuTitle(
            community: Community,
            playerProfile: GameProfile
        ): Text {
            return Text.of("Region Settings - ${playerProfile.name}")
        }
    }
}