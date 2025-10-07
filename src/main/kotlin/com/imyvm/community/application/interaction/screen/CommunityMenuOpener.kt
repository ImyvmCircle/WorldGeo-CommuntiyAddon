package com.imyvm.community.application.interaction.screen

import com.imyvm.community.inter.screen.AbstractMenu
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object CommunityMenuOpener {
    fun open(
        player: ServerPlayerEntity,
        context: Any? = null,
        handlerFactory: (Int, Any?) -> AbstractMenu
    ) {
        player.openHandledScreen(object : NamedScreenHandlerFactory {
            override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity) =
                handlerFactory(syncId, context)

            override fun getDisplayName(): Text =
                handlerFactory(0, context).menuTitle
        })
    }
}