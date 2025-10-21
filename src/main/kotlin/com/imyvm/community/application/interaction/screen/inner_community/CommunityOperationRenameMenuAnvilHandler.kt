package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.CommunityOperationMenu
import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationRenameMenuAnvilHandler {
    fun reopenOperationMenuWithNewName(player: ServerPlayerEntity, community: Community) {
        CommunityMenuOpener.open(player, null) { newSyncId, _ ->
            CommunityOperationMenu(newSyncId, community, player)
        }
    }

    fun getTitle(): Text {
        return Translator.tr("ui.community.operation.rename.title") ?: Text.of("Rename Community")
    }
}