package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractListMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationRegionMenu(
    syncId: Int,
    val community: Community,
    private val isGeographic: Boolean,
    val playerExecutor: ServerPlayerEntity,
    page: Int = 0
): AbstractListMenu(
    syncId,
    menuTitle = generateMenuTitle(community, isGeographic, playerExecutor),
    page = page
) {

    private val unitsPerPage = 35
    private val startSlot = 10
    private val endSlot = 44
    private val unitsInPageZero = unitsPerPage - 2
    private val startSlotInPageZero = startSlot + 2

    init {
        if (page == 0) addGlobalButton()
        addLocalButton()
    }

    private fun addGlobalButton() {
        addButton(
            slot = 10,
            name = Translator.tr("ui.community.operation.region.global")?.string ?: "Region Global",
            item = Items.ELYTRA
        ) {}
    }

    private fun addLocalButton() {
        val scopes = community.getRegion()?.geometryScope?: return
        val scopesInPage = if (page == 0) {
            scopes.take(unitsInPageZero)
        } else {
            scopes.drop((page - 1) * unitsPerPage + unitsInPageZero).take(unitsPerPage)
        }

        var slotIndex = if (page == 0) startSlotInPageZero else startSlot
        for (scope in scopesInPage) {

            val item = when (slotIndex % 9) {
                0 -> Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE
                1 -> Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE
                2 -> Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE
                3 -> Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE
                4 -> Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE
                5 -> Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE
                6 -> Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE
                7 -> Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE
                else -> Items.ITEM_FRAME
            }

            addButton(
                slot = slotIndex,
                name = scope.scopeName,
                item = item
            ) { executeScope(scope) }

            slotIndex = incrementSlotIndex(slotIndex)
            if (slotIndex > endSlot) break
        }
    }

    override fun calculateTotalPages(listSize: Int): Int {
        return (listSize + 2 + unitsPerPage - 1) / unitsPerPage
    }

    override fun openNewPage(player: ServerPlayerEntity, newPage: Int) {
        CommunityMenuOpener.open(player) { syncId ->
            CommunityOperationRegionMenu(syncId, community, isGeographic, player, newPage)
        }
    }

    private fun executeScope(scope: GeoScope) {
        if (isGeographic) {
            val communityRegion = community.getRegion()
            communityRegion?.let { PlayerInteractionApi.modifyScope(playerExecutor, it, scope.scopeName) }
        } else {
            CommunityMenuOpener.open(playerExecutor) { syncId ->
                RegionalSettingMenu(
                    syncId = syncId,
                    playerExecutor = playerExecutor,
                    community = community,
                    scope = scope
                )
            }
        }
    }

    companion object {
        fun generateMenuTitle(community: Community, isGeographic: Boolean, playerExecutor: ServerPlayerEntity): Text {
            val baseTitle = community.generateCommunityMark() + " - "
            val specificTitle = if (isGeographic) {
                Translator.tr("ui.community.operation.region.geography.title.component")?: "Choose scope to modify geographic shape"
            } else {
                Translator.tr("ui.community.operation.region.setting.title.component")?: "Choose scope to modify region settings"
            }
            return Text.of("$baseTitle$specificTitle: ${playerExecutor.name}")
        }
    }
}