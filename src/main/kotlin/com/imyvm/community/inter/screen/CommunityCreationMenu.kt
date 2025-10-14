package com.imyvm.community.inter.screen

import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.text.Text

class CommunityCreationMenu(
    syncId: Int,
    currentName: String?
) : AbstractMenu(
    syncId,
    menuTitle = Text.of(currentName) ?: Translator.tr("ui.create.title")
) {
    init {
        addButton(
            slot = 10,
            name = menuTitle?.string ?: "Name",
            item = Items.NAME_TAG) {}
    }


}