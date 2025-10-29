package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
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
) : AbstractMenu(
    syncId,
    menuTitle = generateCommunityMemberListMenuTitle(community)
) {
    init {
        if (page == 0) {
            addOwnerButton()
            addAdminButtons()
            addMemberButtons()
        } else {
            addMemberButtons()
        }

        addPageButtons()
    }

    private fun addOwnerButton() {
        addButton(
            slot = 10,
            name = "Owner:",
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
            name = "Admins:",
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
            ) {}
        }

    }

    private fun addMemberButtons() {
        addButton(
            slot = 28,
            name = "Members:",
            item = Items.VILLAGER_SPAWN_EGG
        ) {}

        val memberUUIDs = community.getMemberUUIDs()
        for (uuid in memberUUIDs) {
            val memberName = resolveNameFromUUID(uuid, player)
            val slotIndex = 30 + memberUUIDs.indexOf(uuid)
            addButton(
                slot = slotIndex,
                name = memberName ?: "Unknown Member",
                itemStack = if (memberName != null) {
                    createPlayerHeadItem(memberName, uuid)
                } else {
                    ItemStack(Items.PLAYER_HEAD)
                }
            ) {}
        }
    }

    private fun addPageButtons(){

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
            Text.of(community.generateCommunityMark() + " - Member List")
    }
}