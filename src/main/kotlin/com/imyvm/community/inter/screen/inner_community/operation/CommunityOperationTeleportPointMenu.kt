package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.component.GeoScope
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