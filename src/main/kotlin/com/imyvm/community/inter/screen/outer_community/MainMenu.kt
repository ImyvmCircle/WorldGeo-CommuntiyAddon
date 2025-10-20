package com.imyvm.community.inter.screen.outer_community

import com.imyvm.community.application.interaction.screen.outer_community.runCreate
import com.imyvm.community.application.interaction.screen.outer_community.runList
import com.imyvm.community.application.interaction.screen.outer_community.runMyCommunity
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import net.minecraft.item.Items

class MainMenu(syncId: Int) :
    AbstractMenu(
        syncId,
        menuTitle = Translator.tr("ui.main.title")
    ) {

    init {
        addButton(slot = 10, name = Translator.tr("ui.main.button.list")?.string ?: "List", item = Items.WRITABLE_BOOK) { runList(it) }
        addButton(slot = 13, name = Translator.tr("ui.main.button.create")?.string ?: "Create", item = Items.DIAMOND_PICKAXE) { runCreate(it) }
        addButton(slot = 16, name = Translator.tr("ui.main.button.my")?.string ?: "My Village", item = Items.RED_BED) { runMyCommunity(it) }
    }
}