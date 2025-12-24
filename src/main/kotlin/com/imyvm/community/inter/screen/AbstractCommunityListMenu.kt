package com.imyvm.community.inter.screen

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.component.getPlayerHeadButtonItemStackCommunity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

abstract class AbstractCommunityListMenu(
    syncId: Int,
    menuTitle: Text?,
    page: Int = 0
) : AbstractListMenu(
    syncId,
    menuTitle = menuTitle,
    page
) {

    private val communitiesPerPage = 26
    private val startSlot = 10
    private val endSlot = 35

    protected abstract fun getCommunities(): List<Community>

    fun addCommunityButtons() {
        val communityList = getCommunities().drop(page * communitiesPerPage).take(communitiesPerPage)
        var slot = startSlot

        for (community in communityList) {
            if (!addPlayerHeadButton(slot, community) { player -> onCommunityButtonClick(player, community) }) continue

            slot = super.incrementSlotIndex(slot)
            if (slot > endSlot) break
        }
    }

    protected open fun onCommunityButtonClick(player: ServerPlayerEntity, community: Community) {
        CommunityMenuOpener.openCommunityMenu(player, community)
    }

    override fun calculateTotalPages(listSize: Int): Int {
        return ((listSize + communitiesPerPage - 1) / communitiesPerPage)
    }

    override fun openNewPage(player: ServerPlayerEntity, newPage: Int) {
        CommunityMenuOpener.open(player) { syncId -> createNewMenu(syncId, newPage) }
    }

    protected abstract fun createNewMenu(syncId: Int, newPage: Int): AbstractListMenu

    private fun addPlayerHeadButton(slot: Int, community: Community, onClick: (ServerPlayerEntity) -> Unit): Boolean {
        addButton(
            slot = slot,
            name = community.generateCommunityMark(),
            itemStack = getPlayerHeadButtonItemStackCommunity(community), onClick = onClick
        )
        return true
    }
}

