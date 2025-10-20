package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class OperationMenu(
    syncId: Int,
    community: Community,
    player: ServerPlayerEntity
): AbstractMenu(
    syncId,
    menuTitle = Text.of(
        community.generateCommunityMark()
                + " - " + Translator.tr("ui.community.operation.title")?.string
                + ":" + player.name.string
    )
){
    init {
        addButton(
            slot = 10,
            name = Translator.tr("ui.community.operation.button.name")?.string ?: "Community Name",
            item = net.minecraft.item.Items.NAME_TAG
        ) {}
    }
}