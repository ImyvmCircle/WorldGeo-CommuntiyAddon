package com.imyvm.community.inter.screen.outer_community

import com.imyvm.community.application.interaction.screen.outer_community.CommunityCreationRenameMenuAnvilHandler
import com.imyvm.community.inter.screen.AbstractRenameMenuAnvil
import com.imyvm.iwg.domain.Region
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityCreationRenameMenuAnvil(
    player: ServerPlayerEntity,
    initialName: String,
    private val currentShape: Region.Companion.GeoShapeType,
    private val isManor: Boolean,
    private val renameHandler: CommunityCreationRenameMenuAnvilHandler = CommunityCreationRenameMenuAnvilHandler()
) : AbstractRenameMenuAnvil(player, initialName) {

    override fun processRenamingByNewName(finalName: String) =
        renameHandler.reopenCreationMenu(player, finalName, currentShape, isManor)

    override fun getMenuTitle(): Text = renameHandler.getTitle()
}