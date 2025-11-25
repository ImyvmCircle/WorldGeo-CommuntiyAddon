package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
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
    init {
        addPermissionSettingButtons()
        addEffectSettingButtons()
        addRuleSettingButtons()
    }

    private fun addPermissionSettingButtons(){
        addButton(
            slot = 10,
            name = Translator.tr("ui.community.operation.region.setting.list.permission.header")?.string ?: "Permission Settings",
            item = net.minecraft.item.Items.SHIELD
        ) {}


    }

    private fun addEffectSettingButtons(){
        addButton(
            slot = 19,
            name = Translator.tr("ui.community.operation.region.setting.list.effect.header")?.string ?: "Effect Settings",
            item = net.minecraft.item.Items.BEACON
        ) {}
    }

    private fun addRuleSettingButtons(){
        addButton(
            slot = 28,
            name = Translator.tr("ui.community.operation.region.setting.list.rule.header")?.string ?: "Rule Settings",
            item = net.minecraft.item.Items.WRITABLE_BOOK
        ) {}

    }

    companion object {
        fun generateRegionSettingMenuTitle(
            community: Community,
            scope: GeoScope? = null,
            playerProfile: GameProfile? = null
        ): Text {
            val nullTag = Translator.tr("ui.community.operation.region.setting.list.title.unknown")?.string ?: "Unknown"
            val settingTag = Translator.tr("ui.community.operation.region.setting.list.title.setting")?.string ?: "Setting"
            var menuTitle = (community.getRegion()?.name ?: nullTag) + settingTag
            if (scope != null) menuTitle += " - ${scope.scopeName}"
            if (playerProfile != null) menuTitle += " - ${playerProfile.name}"
            return Text.of(menuTitle)
        }
    }
}