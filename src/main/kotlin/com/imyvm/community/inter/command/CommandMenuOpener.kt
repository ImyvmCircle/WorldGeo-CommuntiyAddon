package com.imyvm.community.inter.command

import com.imyvm.community.inter.screen.AbstractMenuHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object CommandMenuOpener {
    fun open(player: ServerPlayerEntity, handlerFactory: (Int) -> AbstractMenuHandler) {
        player.openHandledScreen(object : NamedScreenHandlerFactory {
            override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity) =
                handlerFactory(syncId)
            override fun getDisplayName(): Text = handlerFactory(0).menuTitle
        })
    }
}