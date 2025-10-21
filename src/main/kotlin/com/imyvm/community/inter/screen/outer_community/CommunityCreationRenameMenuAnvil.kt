package com.imyvm.community.inter.screen.outer_community

import com.imyvm.community.application.interaction.screen.outer_community.CommunityCreationRenameMenuHandlerAnvil
import com.imyvm.community.inter.screen.AbstractRenameMenuAnvil
import com.imyvm.iwg.domain.Region
import net.minecraft.server.network.ServerPlayerEntity

class CommunityCreationRenameMenuAnvil(
    player: ServerPlayerEntity,
    initialName: String,
    private val currentShape: Region.Companion.GeoShapeType,
    private val isManor: Boolean,
    private val renameHandler: CommunityCreationRenameMenuHandlerAnvil = CommunityCreationRenameMenuHandlerAnvil()
) : AbstractRenameMenuAnvil(player, initialName) {

    override fun onConfirmRename(finalName: String) {
        renameHandler.processRenameClose(player, finalName, currentShape, isManor)
    }

    override fun getMenuTitle(): String = "ui.create.rename.title"
}