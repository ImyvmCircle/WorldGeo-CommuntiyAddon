package com.imyvm.community.inter.screen

import com.imyvm.community.application.interaction.common.filterCommunitiesByType
import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.application.interaction.screen.runSwitchMode
import com.imyvm.community.domain.Community
import com.imyvm.community.util.Translator
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityListMenu(
    syncId: Int,
    private val mode: String = "JOIN-ABLE",
    page: Int = 0
) : AbstractCommunityListMenu(syncId, Translator.tr("ui.list.title"), page) {

    init {
        addModeButtons(mode)
    }

    override fun createNewMenu(newPage: Int): NamedScreenHandlerFactory {
        return CommunityListMenuFactory(mode, newPage)
    }

    override fun getCommunities(): List<Community> {
        return filterCommunitiesByType(this.mode)
    }

    override fun onCommunityButtonClick(player: ServerPlayerEntity, community: Community) {
        CommunityMenuOpener.openCommunityMenu(player, community)
    }

    private fun addModeButtons(content: String) {
        val current = content.uppercase()

        val modeColorMap = mapOf(
            "ALL" to Items.ORANGE_WOOL,
            "JOIN-ABLE" to Items.GREEN_WOOL,
            "RECRUITING" to Items.LIME_WOOL,
            "AUDITING" to Items.YELLOW_WOOL,
            "ACTIVE" to Items.CYAN_WOOL,
            "REVOKED" to Items.RED_WOOL
        )

        val selectedItem = modeColorMap[current] ?: Items.WHITE_WOOL

        addButton(
            slot = 45,
            name = Translator.tr("ui.list.button.${current.lowercase()}")?.string ?: current,
            item = selectedItem
        ) {}

        addButton(slot = 47, name = "All", item = Items.ORANGE_WOOL) { runSwitchMode(it, "ALL") }
        addButton(slot = 48, name = "Join-able", item = Items.GREEN_WOOL) { runSwitchMode(it, "JOIN-ABLE") }
        addButton(slot = 49, name = "Recruiting", item = Items.LIME_WOOL) { runSwitchMode(it, "RECRUITING") }
        addButton(slot = 50, name = "Auditing", item = Items.YELLOW_WOOL) { runSwitchMode(it, "AUDITING") }
        addButton(slot = 51, name = "Active", item = Items.CYAN_WOOL) { runSwitchMode(it, "ACTIVE") }
        addButton(slot = 52, name = "Revoked", item = Items.RED_WOOL) { runSwitchMode(it, "REVOKED") }
    }
}

class CommunityListMenuFactory(
    private val mode: String,
    private val page: Int
) : NamedScreenHandlerFactory {

    override fun createMenu(syncId: Int, inv: net.minecraft.entity.player.PlayerInventory, player: PlayerEntity): ScreenHandler {
        return CommunityListMenu(syncId, mode, page)
    }

    override fun getDisplayName(): Text =
        Translator.tr("ui.list.title") ?: Text.literal("Community List")
}