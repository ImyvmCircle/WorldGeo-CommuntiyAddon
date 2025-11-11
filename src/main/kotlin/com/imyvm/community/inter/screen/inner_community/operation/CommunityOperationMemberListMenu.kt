package com.imyvm.community.inter.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.application.interaction.screen.inner_community.operation.runCommunityOperationMember
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractListMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import com.imyvm.iwg.util.translator.resolvePlayerName
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import java.util.*

class CommunityOperationMemberListMenu(
    syncId: Int,
    val community: Community,
    val player: ServerPlayerEntity,
    private val page: Int = 0
) : AbstractListMenu(
    syncId,
    menuTitle = generateCommunityMemberListMenuTitle(community),
    page
) {

    private val playersPerPage = 35
    private val startSlot = 10
    private val endSlot = 44
    private val playersInPageZero = playersPerPage - 2 * 7
    private val startSlotInPageZero = startSlot + 2 * 9 + 2

    init {
        if (page == 0) {
            addOwnerButton()
            addAdminButtons()
            addMemberButtons()
        } else {
            addMemberButtons()
        }

        handlePage(community.getMemberUUIDs().size)
    }

    private fun addOwnerButton() {
        addButton(
            slot = 10,
            name = (Translator.tr("ui.community.operation.member_list.owner")?.string ?: "Owner") + ":",
            item = Items.COMMAND_BLOCK
        ) {}

        val ownerUUID = community.getOwnerUUID()
        val ownerName = resolveNameFromUUID(ownerUUID, player)
        addButton(
            slot = 12,
            name = ownerName,
            itemStack = if (ownerName != null) {
                createPlayerHeadItem(ownerName, ownerUUID!!)
            } else {
                ItemStack(Items.PLAYER_HEAD)
            }
        ) {}
    }

    private fun addAdminButtons() {
        addButton(
            slot = 19,
            name = (Translator.tr("ui.community.operation.member_list.admin")?.string ?: "Admins") + ":",
            item = Items.COMMAND_BLOCK_MINECART
        ) {}

        val adminUUIDs = community.getAdminUUIDs()
        for (uuid in adminUUIDs) {
            val adminName = resolveNameFromUUID(uuid, player)
            val slotIndex = 21 + adminUUIDs.indexOf(uuid)
            addButton(
                slot = slotIndex,
                name = adminName ?: "Unknown Admin",
                itemStack = if (adminName != null) {
                    createPlayerHeadItem(adminName, uuid)
                } else {
                    ItemStack(Items.PLAYER_HEAD)
                }
            ) { runCommunityOperationMember(community, uuid, player) }
        }

    }

    private fun addMemberButtons() {
        addButton(
            slot = 28,
            name = (Translator.tr("ui.community.operation.member_list.member")?.string ?: "Members") + ":",
            item = Items.VILLAGER_SPAWN_EGG
        ) {}

        val memberUUIDs = community.getMemberUUIDs()
        val memberInPageList = if (page == 0) {
            memberUUIDs.take(playersInPageZero)
        } else {
            memberUUIDs.drop((page - 1) * playersPerPage + playersInPageZero).take(playersPerPage)
        }

        var slotIndex = if (page == 0) startSlotInPageZero else startSlot
        for (uuid in memberInPageList) {
            val memberName = resolveNameFromUUID(uuid, player)

            addButton(
                slot = slotIndex,
                name = memberName ?: "Unknown Member",
                itemStack = if (memberName != null) {
                    createPlayerHeadItem(memberName, uuid)
                } else {
                    ItemStack(Items.PLAYER_HEAD)
                }
            ) { runCommunityOperationMember(community, uuid, player) }

            slotIndex = super.incrementSlotIndex(slotIndex)
            if (slotIndex > endSlot) break
        }
    }

    override fun calculateTotalPages(listSize: Int): Int {
        return (listSize + 2 * 7 + 2 + playersPerPage - 1 / playersPerPage)
    }

    override fun openNewPage(player: ServerPlayerEntity, newPage: Int) {
        CommunityMenuOpener.open(player) { syncId -> CommunityOperationMemberListMenu(syncId, community, player, newPage) }
    }

    @Deprecated(
        message = "Temporary implementation. Use UtilApi.getPlayerName() after dependency upgrade",
        replaceWith = ReplaceWith("UtilApi.getPlayerName(player, ownerUUID)")
    )
    private fun resolveNameFromUUID(ownerUUID: UUID?, player: ServerPlayerEntity): String? {
        return player.getServer()?.let { resolvePlayerName(it, ownerUUID) }
    }

    companion object {
        fun generateCommunityMemberListMenuTitle(community: Community): Text =
            Text.of(community.generateCommunityMark()
                    + (Translator.tr("ui.community.operation.member_list.title.component")?.string ?: "- Member List")
            )
    }
}