package com.imyvm.community.inter.command

import com.imyvm.community.inter.screen.MainMenuHandler
import com.imyvm.community.util.Translator
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object CommandMenuOpener {
    fun openCommunityMenu(player: ServerPlayerEntity) {
        player.openHandledScreen(object : NamedScreenHandlerFactory {
            override fun createMenu(
                syncId: Int,
                inv: net.minecraft.entity.player.PlayerInventory,
                player: PlayerEntity
            ) = MainMenuHandler(syncId, inv)

            override fun getDisplayName(): Text? = Translator.tr("ui.main.title")
        })
    }
}