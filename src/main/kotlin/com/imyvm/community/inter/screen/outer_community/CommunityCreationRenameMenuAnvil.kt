package com.imyvm.community.inter.screen.outer_community

import com.imyvm.community.application.interaction.screen.RenameMenuHandlerAnvil
import com.imyvm.community.inter.screen.AbstractRenameMenuAnvil
import com.imyvm.iwg.domain.Region
import net.minecraft.server.network.ServerPlayerEntity

class CommunityCreationRenameMenuAnvil(
    player: ServerPlayerEntity,
    initialName: String,
    private val currentShape: Region.Companion.GeoShapeType,
    private val isManor: Boolean,
    private val renameHandler: RenameMenuHandlerAnvil = RenameMenuHandlerAnvil()
) : AbstractRenameMenuAnvil(player, initialName) {

    override fun onNameChanged(newName: String?) {
        renameHandler.setNewName(newName)
    }

    override fun onConfirmRename() {
        renameHandler.processRenameClose(player, initialName, currentShape, isManor)
    }

    override fun getMenuTitle(): String = "ui.create.rename.title"
}