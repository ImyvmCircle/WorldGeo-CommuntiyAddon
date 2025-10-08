package com.imyvm.community.inter.screen

import com.imyvm.community.application.interaction.common.filterCommunitiesByType
import com.imyvm.community.application.interaction.screen.runSwitchMode
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.text.Text

class CommunityListMenu(syncId: Int, content: Any? = null) :
    AbstractMenu(syncId, menuTitle = Translator.tr("ui.list.title") ?: Text.literal("Community List")) {

    init {
        addButtonMode(content)
        addButtonCommunityList(content)
    }

    private fun addButtonMode(content: Any? = null) {
        when (content as? String){
            "ALL" -> addButton(slot = 45,
                name = Translator.tr("ui.list.button.all")?.string ?: "All",
                item = Items.ORANGE_WOOL) {  }
            "RECRUITING" -> addButton(slot = 45,
                name = Translator.tr("ui.list.button.recruiting")?.string ?: "Recruiting",
                item = Items.LIME_WOOL) {  }
            "AUDITING" -> addButton(slot = 45,
                name = Translator.tr("ui.list.button.auditing")?.string ?: "Auditing",
                item = Items.YELLOW_WOOL) {  }
            "ACTIVE" -> addButton(slot = 45,
                name = Translator.tr("ui.list.button.active")?.string ?: "Active",
                item = Items.CYAN_WOOL) {  }
            else -> addButton(slot = 45,
                name = Translator.tr("ui.list.button.unknown")?.string ?: "All",
                item = Items.WHITE_WOOL) {  }
        }

        addButton(slot = 47,
            name = Translator.tr("ui.list.button.all.alter")?.string ?: "All",
            item = Items.ORANGE_WOOL) { runSwitchMode(it, "ALL") }
        addButton(slot = 48,
            name = Translator.tr("ui.list.button.recruiting.alter")?.string ?: "Recruiting",
            item = Items.LIME_WOOL) { runSwitchMode(it, "RECRUITING") }
        addButton(slot = 49,
            name = Translator.tr("ui.list.button.auditing.alter")?.string ?: "Auditing",
            item = Items.YELLOW_WOOL) { runSwitchMode(it, "AUDITING") }
        addButton(slot = 50,
            name = Translator.tr("ui.list.button.active.alter")?.string ?: "Active",
            item = Items.CYAN_WOOL) { runSwitchMode(it, "ACTIVE") }
    }

    private fun addButtonCommunityList(content: Any? = null) {
        val communityList = filterCommunitiesByType(content as? String ?: "ALL")
        if (communityList.isNotEmpty()) {
            TODO()
        }
    }

}