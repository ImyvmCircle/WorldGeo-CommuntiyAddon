package com.imyvm.community.application.interaction.screen

import com.imyvm.community.domain.Community
import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.CommunityMenu
import com.imyvm.community.util.Translator
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object CommunityMenuOpener {
    fun <C, M : AbstractMenu> open(
        player: ServerPlayerEntity,
        content: C? = null,
        handlerFactory: (syncId: Int, content: C?) -> M
    ) {
        player.openHandledScreen(object : NamedScreenHandlerFactory {
            override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): M =
                handlerFactory(syncId, content)

            override fun getDisplayName(): Text? =
                handlerFactory(0, content).menuTitle
        })
    }

    fun openCommunityMenu(player: ServerPlayerEntity, regionId: Int) {
        if (regionId == -1) {
            player.sendMessage(Translator.tr("ui.error.wrong_region_id"))
            return player.closeHandledScreen()
        }

        val community = CommunityDatabase.getCommunityById(regionId)
            ?: return player.sendMessage(Translator.tr("ui.error.region_not_found")).also {
                player.closeHandledScreen()
            }

        val content: Pair<ServerPlayerEntity, Community> = player to community
        open(player, content) { syncId, c ->
            CommunityMenu(syncId, c!!)
        }
    }

}