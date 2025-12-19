package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.inner_community.operation.getPermissionButtonItemStack
import com.imyvm.community.application.interaction.screen.inner_community.operation.togglePermissionSetting
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.domain.component.PermissionKey
import com.mojang.authlib.GameProfile
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class RegionalSettingMenu(
    syncId: Int,
    val playerExecutor: ServerPlayerEntity,
    val community: Community,
    val scope: GeoScope? = null,
    private val playerProfile: GameProfile? = null
) : AbstractMenu(
    syncId,
    menuTitle = generateRegionSettingMenuTitle(
        community = community,
        scope = scope,
        playerProfile = playerProfile
    )
) {
    init {
        addPermissionSettingButtons()
        addEffectSettingButtons()
        if (playerProfile == null) addRuleSettingButtons()
    }

    private fun addPermissionSettingButtons(){
        addButton(
            slot = 10,
            name = Translator.tr("ui.community.operation.region.setting.list.permission.header")?.string ?: "Permission Settings",
            item = Items.SHIELD
        ) {}

        addButton(
            slot = 12,
            name = Translator.tr("ui.community.operation.region.setting.list.permission.build_break")?.string ?: "Build/Break Permission",
            itemStack = getPermissionButtonItemStack(Items.DIAMOND_PICKAXE, community, scope, playerProfile, PermissionKey.BUILD_BREAK)
        ) { togglePermissionSetting(playerExecutor, community, scope, playerProfile, PermissionKey.BUILD_BREAK) }

        addButton(
            slot = 13,
            name = Translator.tr("ui.community.operation.region.setting.list.permission.fly")?.string ?: "Fly Permission",
            itemStack = getPermissionButtonItemStack(Items.ELYTRA, community, scope, playerProfile, PermissionKey.FLY)
        ) { togglePermissionSetting(playerExecutor, community, scope, playerProfile, PermissionKey.FLY)  }

        addButton(
            slot = 14,
            name = Translator.tr("ui.community.operation.region.setting.list.permission.container")?.string ?: "Container Permission",
            itemStack = getPermissionButtonItemStack(Items.CHEST, community, scope, playerProfile, PermissionKey.CONTAINER)
        ) { togglePermissionSetting(playerExecutor, community, scope, playerProfile, PermissionKey.CONTAINER) }
    }

    private fun addEffectSettingButtons(){
        addButton(
            slot = 28,
            name = Translator.tr("ui.community.operation.region.setting.list.effect.header")?.string ?: "Effect Settings",
            item = Items.BEACON
        ) {}
    }

    private fun addRuleSettingButtons(){
        addButton(
            slot = 46,
            name = Translator.tr("ui.community.operation.region.setting.list.rule.header")?.string ?: "Rule Settings",
            item = Items.WRITABLE_BOOK
        ) {}

    }

    companion object {
        private fun generateRegionSettingMenuTitle(
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