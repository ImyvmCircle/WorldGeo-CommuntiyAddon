package com.imyvm.community.inter.screen

import com.imyvm.community.util.Translator
import net.minecraft.text.Text

class CommunityMenu(
    syncId: Int,
    private val content: Any? = null,
) : AbstractMenu(
        syncId,
        menuTitle = Translator.tr("ui.community.title") ?: Text.literal("Community Menu")
    ){
    init {
        addButton(
            slot = 10,
            name = Translator.tr("ui.community.button.list")?.string ?: "Member List",
            item = net.minecraft.item.Items.PAPER
        ) { }
    }
}