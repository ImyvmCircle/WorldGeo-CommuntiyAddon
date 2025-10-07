package com.imyvm.community.inter.screen

import com.imyvm.community.util.Translator
import net.minecraft.text.Text

class CommuntiyListMenuHandler(syncId: Int) :
    AbstractMenuHandler(
        syncId,
        menuTitle = Translator.tr("ui.list.title") ?: Text.literal("Community List")) {

        init {

        }
    }