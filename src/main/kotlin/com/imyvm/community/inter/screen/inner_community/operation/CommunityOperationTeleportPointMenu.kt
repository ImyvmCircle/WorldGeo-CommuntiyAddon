package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.inter.api.RegionDataApi
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationTeleportPointMenu(
    syncId: Int,
    playerExecutor: ServerPlayerEntity,
    community: Community,
    scope: GeoScope
): AbstractMenu(
    syncId = syncId,
    menuTitle = generateTeleportPointManagingMenuTitle(community, scope)
) {
    init {
        addButton(
            slot = 10,
            name = Translator.tr("ui.community.operation.teleport_point.set")?.string ?: "Set Location of Current Feet as Teleport Point",
            item = Items.COMPASS
        ) {}

        addButton(
            slot = 12,
            name = Translator.tr("ui.community.operation.teleport_point.reset")?.string ?: "Reset current teleport point to null.",
            item = Items.BARRIER
        ) {}

        addButton(
            slot = 14,
            name = Translator.tr("ui.community.operation.teleport_point.toogle")?.string ?: "Toggle teleport point accessibility.",
            item = if(RegionDataApi.inquireTeleportPointAccessibility(scope)) {
                Items.RED_WOOL
            } else {
                Items.GREEN_WOOL
            }
        ) {}

        addButton(
            slot = 16,
            name = Translator.tr("ui.community.operation.teleport_point.teleport")?.string ?: "Teleport to the teleport point of this scope.",
            item = Items.ENDER_PEARL
        ) {}
    }
    companion object {
        private fun generateTeleportPointManagingMenuTitle(community: Community, scope: GeoScope): Text {
            return Text.of(
                Translator.tr("ui.community.operation.teleport_point.title",
                    community,
                    scope
                ))
        }
    }
}