package com.imyvm.community.application.interaction.screen.inner_community.operation

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.iwg.domain.component.GeoScope
import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class RegionalSettingMenu(
    syncId: Int,
    playerExecutor: ServerPlayerEntity,
    val community: Community,
    val scope: GeoScope? = null,
    private val playerProfile: GameProfile? = null
) : AbstractMenu(
    syncId,
    menuTitle = generateRegionSettingMenuTitle(
        community = community,
        playerProfile = playerProfile
    )
) {
    companion object {
        fun generateRegionSettingMenuTitle(
            community: Community,
            scope: GeoScope? = null,
            playerProfile: GameProfile? = null
        ): Text {
            var menuTitle = "${community.getRegion()?.name ?: "Unknown"} Settings"
            if (scope != null) menuTitle += " - ${scope.scopeName}"
            if (playerProfile != null) menuTitle += " - ${playerProfile.name}"
            return Text.of(menuTitle)
        }
    }
}