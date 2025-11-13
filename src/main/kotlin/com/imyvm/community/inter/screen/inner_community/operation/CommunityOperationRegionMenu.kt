package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationRegionMenu(
    syncId: Int,
    community: Community,
    isGeographic: Boolean,
    playerExecutor: ServerPlayerEntity
): AbstractMenu(
    syncId,
    menuTitle = generateMenuTitle(community, isGeographic, playerExecutor)
) {

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