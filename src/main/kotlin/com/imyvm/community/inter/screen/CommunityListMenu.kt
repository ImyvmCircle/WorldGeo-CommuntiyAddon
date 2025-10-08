package com.imyvm.community.inter.screen

import com.imyvm.community.application.interaction.common.filterCommunitiesByType
import com.imyvm.community.application.interaction.screen.runSwitchMode
import com.imyvm.community.domain.CommunityRole
import com.imyvm.community.util.Translator
import com.mojang.authlib.properties.PropertyMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ProfileComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import java.util.*

class CommunityListMenu(
    syncId: Int,
    private val content: Any? = null,
    private val page: Int = 0
) : AbstractMenu(
    syncId,
    menuTitle = Translator.tr("ui.list.title") ?: Text.literal("Community List")
) {

    private val communitiesPerPage = 26
    private val startSlot = 10
    private val endSlot = 35

    init {
        addModeButtons(content)
        addCommunityButtons()
        addPageButtons()
    }

    private fun addModeButtons(content: Any? = null) {
        val current = (content as? String)?.uppercase() ?: "ALL"

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

    private fun addCommunityButtons() {
        val communityList = filterCommunitiesByType(content as? String ?: "ALL")
        if (communityList.isEmpty()) return

        val paged = communityList.drop(page * communitiesPerPage).take(communitiesPerPage)

        var slot = startSlot
        for (community in paged) {
            val owner = community.member.entries.find { it.value == CommunityRole.OWNER }?.key ?: continue
            val displayName = community.getRegion()?.name ?: "Community #${community.regionNumberId}"

            addPlayerHeadButton(slot, displayName, owner) { player ->
                community.sendCommunityDescription(player)
            }

            slot++
            if (slot > endSlot) break
        }
    }

    private fun addPageButtons() {
        val communityList = filterCommunitiesByType(content as? String ?: "ALL")
        val totalPages = (communityList.size + communitiesPerPage - 1) / communitiesPerPage

        if (page > 0) {
            addButton(slot = 0, name = Translator.tr("ui.list.prev")?.string ?: "Previous", itemStack = ItemStack(Items.ARROW)) {
                openNewPage(it, page - 1)
            }
        }

        if (page < totalPages - 1) {
            addButton(slot = 8, name = Translator.tr("ui.list.next")?.string ?: "Next", itemStack = ItemStack(Items.ARROW)) {
                openNewPage(it, page + 1)
            }
        }
    }

    private fun openNewPage(player: ServerPlayerEntity, newPage: Int) {
        player.openHandledScreen(CommunityListMenuFactory(content, newPage))
    }

    private fun addPlayerHeadButton(slot: Int, name: String, uuid: UUID, onClick: (ServerPlayerEntity) -> Unit) {
        val headStack = ItemStack(Items.PLAYER_HEAD)
        headStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(name))
        addButton(slot = slot, name = name, itemStack = headStack, onClick = onClick)

        val profileComponent = ProfileComponent(Optional.empty(), Optional.of(uuid), PropertyMap())
        profileComponent.future.thenAccept { fullProfile ->
            headStack.set(DataComponentTypes.PROFILE, fullProfile)
        }
    }
}

class CommunityListMenuFactory(
    private val content: Any?,
    private val page: Int
) : NamedScreenHandlerFactory {

    override fun createMenu(syncId: Int, inv: net.minecraft.entity.player.PlayerInventory, player: PlayerEntity): ScreenHandler {
        return CommunityListMenu(syncId, content, page)
    }

    override fun getDisplayName(): Text =
        Translator.tr("ui.list.title") ?: Text.literal("Community List")
}

