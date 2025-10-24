package com.imyvm.community.application.interaction.screen.inner_community

import com.imyvm.community.application.interaction.common.onCommunityRegionInteraction
import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.CommunityOperationMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationRenameMenuAnvilHandler {

    fun handleProcessRename(player: ServerPlayerEntity, community: Community, newName: String){
        if (renameCommunity(player, community, newName) == 0) {
            player.closeHandledScreen()
            return
        }
        reopenOperationMenuWithNewName(player, community)
    }

    fun getTitle(): Text {
        return Translator.tr("ui.community.operation.rename.title") ?: Text.of("Rename Community")
    }

    private fun renameCommunity(player: ServerPlayerEntity, community: Community, newName: String): Int {
        return onCommunityRegionInteraction(player, community) { p, _, region ->
            PlayerInteractionApi.renameRegion(p, region, newName)
        }
    }

    private fun reopenOperationMenuWithNewName(player: ServerPlayerEntity, community: Community) {
        CommunityMenuOpener.open(player, null) { newSyncId, _ ->
            CommunityOperationMenu(newSyncId, community, player)
        }
    }
}