package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractMenu
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationMemberListMenu(
    syncId: Int,
    community: Community,
    player: ServerPlayerEntity
): AbstractMenu(
    syncId,
    menuTitle = generateCommunityMemberListMenuTitle(community)
) {
    init {
        addOwnerButton()
        addAdminButtons()
        addMemberButtons()
    }

    private fun addOwnerButton(){
        addButton(
            slot = 10,
            name = "Owner:",
            item = Items.COMMAND_BLOCK
        ) {}
    }

    private fun addAdminButtons(){
        addButton(
            slot = 19,
            name = "Admins:",
            item = Items.COMMAND_BLOCK_MINECART
        ) {}
    }

    private fun addMemberButtons(){
        addButton(
            slot = 28,
            name = "Members:",
            item = Items.VILLAGER_SPAWN_EGG
        ) {}
    }

   companion object {
       fun generateCommunityMemberListMenuTitle(community: Community): Text =
           Text.of(community.generateCommunityMark() + " - Member List")
   }

}