package com.imyvm.community.inter.screen

import com.imyvm.community.util.Translator
import net.minecraft.text.Text

class CommunityListMenu(syncId: Int, content: Any? = null) :
    AbstractMenu(syncId, menuTitle = Translator.tr("ui.list.title") ?: Text.literal("Community List")) {

    init {
        addButtonMode(content)
        addButtonCommunityList()
    }

    private fun addButtonMode(content: Any? = null) {
        when (content as? String){
            TODO() -> {}
        }
    }

    private fun addButtonCommunityList() {

    }
}