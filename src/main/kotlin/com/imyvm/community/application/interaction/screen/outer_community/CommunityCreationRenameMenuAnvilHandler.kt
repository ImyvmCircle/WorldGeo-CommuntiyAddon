package com.imyvm.community.application.interaction.screen.outer_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.inter.screen.outer_community.CommunityCreationMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.Region
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityCreationRenameMenuAnvilHandler {

    fun reopenCreationMenu(player: ServerPlayerEntity, newName: String, shape: Region.Companion.GeoShapeType, isManor: Boolean) {
        CommunityMenuOpener.open(player, null) { newSyncId, _ ->
            CommunityCreationMenu(newSyncId, newName, shape, isManor, player)
        }
    }

    fun getTitle(): Text {
        return Translator.tr ("ui.create.rename.title") ?: Text.of("Rename Community")
    }
}