package com.imyvm.community.inter.screen

import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class MainMenuHandler(syncId: Int) :
    AbstractMenuHandler(
        syncId,
        menuTitle = Translator.tr("ui.main.title") ?: Text.literal("Community Main Menu")
    ) {

    init {
        addButton(slot = 10, name = Translator.tr("ui.main.button.list")?.string ?: "List", item = Items.WRITABLE_BOOK) { runList(it) }
        addButton(slot = 13, name = Translator.tr("ui.main.button.create")?.string ?: "Create", item = Items.DIAMOND_PICKAXE) { runCreate(it) }
        addButton(slot = 16, name = Translator.tr("ui.main.button.my")?.string ?: "My Village", item = Items.RED_BED) { runMyCommunity(it) }
        addButton(slot = 53, name = Translator.tr("ui.main.button.close")?.string ?: "Close", item = Items.BARRIER) { runClose(it) }
    }

    private fun runList(player: ServerPlayerEntity){
        player.sendMessage(Text.literal("打开聚落列表（未实现）"))
    }

    private fun runCreate(player: ServerPlayerEntity){
        player.sendMessage(Text.literal("创建聚落（未实现）"))
    }

    private fun runMyCommunity(player: ServerPlayerEntity){
        player.sendMessage(Text.literal("打开我的聚落（未实现）"))
    }

    private fun runClose(player: ServerPlayerEntity){
        player.closeHandledScreen()
        player.sendMessage(Translator.tr("ui.main.button.close.feedback"))
    }
}