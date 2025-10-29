package com.imyvm.community.inter.screen

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityRole
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import java.util.*

abstract class AbstractCommunityListMenu(
    syncId: Int,
    menuTitle: Text?,
    private val page: Int = 0
) : AbstractMenu(
    syncId,
    menuTitle = menuTitle
) {

    private val communitiesPerPage = 26
    private val startSlot = 10
    private val endSlot = 35

    protected abstract fun getCommunities(): List<Community>

    fun addCommunityButtons() {
        val communityList = getCommunities().drop(page * communitiesPerPage).take(communitiesPerPage)
        var slot = startSlot

        for (community in communityList) {
            val owner = community.member.entries.find { it.value == CommunityRole.OWNER }?.key ?: continue
            val displayName = community.getRegion()?.name ?: "Community #${community.regionNumberId}"

            addPlayerHeadButton(slot, displayName, owner) { player ->
                onCommunityButtonClick(player, community)
            }

            slot = incrementSlot(slot)
            if (slot > endSlot) break
        }
    }

    protected open fun onCommunityButtonClick(player: ServerPlayerEntity, community: Community) {
        CommunityMenuOpener.openCommunityMenu(player, community)
    }

    fun addPageButtons() {
        val totalPages = ((getCommunities().size) + communitiesPerPage - 1) / communitiesPerPage

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

    private fun incrementSlot(slot: Int): Int {
        return if ((slot + 1) % 9 == 8) slot + 3
        else slot + 1
    }

    private fun openNewPage(player: ServerPlayerEntity, newPage: Int) {
        player.openHandledScreen(createNewMenu(newPage))
    }

    protected abstract fun createNewMenu(newPage: Int): NamedScreenHandlerFactory

    private fun addPlayerHeadButton(slot: Int, name: String, uuid: UUID, onClick: (ServerPlayerEntity) -> Unit) {
        addButton(slot = slot, name = name, itemStack = createPlayerHeadItem(name, uuid), onClick = onClick)
    }
}

