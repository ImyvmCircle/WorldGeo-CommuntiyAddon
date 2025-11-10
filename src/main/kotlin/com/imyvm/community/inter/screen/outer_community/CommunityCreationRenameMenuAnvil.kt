package com.imyvm.community.inter.screen.outer_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.inter.screen.AbstractRenameMenuAnvil
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.Region
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityCreationRenameMenuAnvil(
    player: ServerPlayerEntity,
    initialName: String,
    private val currentShape: Region.Companion.GeoShapeType,
    private val isManor: Boolean,
) : AbstractRenameMenuAnvil(player, initialName) {

    override fun processRenaming(finalName: String) {
        CommunityMenuOpener.open(player) { newSyncId ->
            CommunityCreationMenu(newSyncId, finalName, currentShape, isManor, player)
        }
    }


    override fun getMenuTitle(): Text = Translator.tr ("ui.create.rename.title") ?: Text.of("Rename Community")
}