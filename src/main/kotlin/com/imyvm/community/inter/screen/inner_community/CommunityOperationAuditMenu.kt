package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationAuditMenu(
    syncId: Int,
    community: Community,
    playerExecutor: ServerPlayerEntity
): AbstractMenu(
    syncId,
    menuTitle = generateMenuTitle(community)
) {
    companion object {
        fun generateMenuTitle(community: Community): Text = Text.of(community.generateCommunityMark() + " - Audit Requests:")
    }
}