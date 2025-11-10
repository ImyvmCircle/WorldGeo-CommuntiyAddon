package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.text.Text

class CommunityOperationAuditMenu(
    syncId: Int,

): AbstractMenu(
    syncId,
    menuTitle = generateCommunityOperationAuditMenuTitle()
) {
    init {
        addButton(
            slot = 21,
            name = Translator.tr("ui.community.operation.audit.button.accept") ?.string ?: "Accept",
            item = Items.GREEN_WOOL
        ) {}

        addButton(
            slot = 26,
            name = Translator.tr("ui.community.operation.audit.button.refuse") ?.string ?: "Refuse",
            item = Items.BARRIER
        ) {}
    }
    companion object {
        fun generateCommunityOperationAuditMenuTitle(): Text = Translator.tr("ui.community.operation.audit.title") ?: Text.of("Audit")
    }
}