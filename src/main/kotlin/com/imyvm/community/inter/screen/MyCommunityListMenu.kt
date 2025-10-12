package com.imyvm.community.inter.screen

import com.imyvm.community.domain.Community
import com.imyvm.community.util.Translator
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text

class MyCommunityListMenu(
    syncId: Int,
    private val joinedCommunities: List<Community>,
    page: Int = 0
) : AbstractCommunityListMenu(syncId, Translator.tr("ui.my_communities.title"), page) {

    override fun createNewMenu(newPage: Int): NamedScreenHandlerFactory {
        return MyCommunityListMenuFactory(joinedCommunities, newPage)
    }

    override fun getCommunities(): List<Community> {
        return joinedCommunities
    }
}

class MyCommunityListMenuFactory(
    private val joinedCommunities: List<Community>,
    private val page: Int
) : NamedScreenHandlerFactory {

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return MyCommunityListMenu(syncId, joinedCommunities, page)
    }

    override fun getDisplayName(): Text =
        Translator.tr("ui.my_communities.title") ?: Text.literal("My Communities")
}