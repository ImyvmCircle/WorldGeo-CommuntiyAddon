package com.imyvm.community.application.interaction.screen.outer_community

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.inter.screen.outer_community.CommunityCreationMenu
import com.imyvm.iwg.domain.Region
import net.minecraft.server.network.ServerPlayerEntity

class CommunityCreationRenameMenuHandler {
    private var capturedName: String = ""
    private var shouldReopen: Boolean = true

    fun setNewName(name: String?) {
        capturedName = name?.trim() ?: ""
    }

    fun processRenameClose(player: ServerPlayerEntity, currentName: String, shape: Region.Companion.GeoShapeType, isManor: Boolean) {
        if (shouldReopen) {
            shouldReopen = false
            val newName = capturedName.takeIf { it.isNotEmpty() } ?: currentName
            reopenCommunityCreation(player, newName, shape, isManor)
        }
    }

    private fun reopenCommunityCreation(
        player: ServerPlayerEntity,
        newName: String,
        shape: Region.Companion.GeoShapeType,
        isManor: Boolean
    ) {
        CommunityMenuOpener.open(player, null) { newSyncId, _ ->
            CommunityCreationMenu(newSyncId, newName, shape, isManor, player)
        }
    }
}
