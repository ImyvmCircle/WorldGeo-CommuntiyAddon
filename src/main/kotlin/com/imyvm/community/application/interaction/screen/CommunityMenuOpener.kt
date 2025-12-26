package com.imyvm.community.application.interaction.screen

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.inner_community.CommunityMenu
import com.imyvm.community.inter.screen.inner_community.operation_only.CommunityOperationRenameMenuAnvil
import com.imyvm.community.inter.screen.outer_community.CommunityCreationRenameMenuAnvil
import com.imyvm.community.inter.screen.outer_community.MainMenu
import com.imyvm.iwg.domain.component.GeoShapeType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object CommunityMenuOpener {
    fun <M : AbstractMenu> open(
        player: ServerPlayerEntity,
        handlerFactory: (syncId: Int) -> M
    ) {
        player.openHandledScreen(object : NamedScreenHandlerFactory {
            override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): M =
                handlerFactory(syncId)

            override fun getDisplayName(): Text? =
                handlerFactory(0).menuTitle
        })
    }

    fun openCommunityMenu(player: ServerPlayerEntity, community: Community) {
        open(player) { syncId -> CommunityMenu(syncId, player, community) {
            open(player) {
                MainMenu(syncId = syncId) } }
        }
    }

    fun openCreationRenameAnvilMenu(
        player: ServerPlayerEntity,
        currentName: String,
        currentShape: GeoShapeType,
        isManor: Boolean
    ) {
        val handler = CommunityCreationRenameMenuAnvil(player, currentName, currentShape, isManor)
        handler.open()
    }

    fun openCommunityRenameAnvilMenu(
        player: ServerPlayerEntity,
        community: Community
    )  {
        val handler = CommunityOperationRenameMenuAnvil(player, community = community)
        handler.open()
    }
}