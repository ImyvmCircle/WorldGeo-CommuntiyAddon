package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationAdvancementMenu(
    syncId: Int,
    community: Community,
    playerExecutor: ServerPlayerEntity
): AbstractMenu(
    syncId,
    menuTitle = generateCommunityAdvancementMenuTitle(community)
) {
    companion object {
        private fun generateCommunityAdvancementMenuTitle(community: Community): Text {
            return Text.of(community.generateCommunityMark() + " - Community Advancement Menu: ")
        }
    }
}