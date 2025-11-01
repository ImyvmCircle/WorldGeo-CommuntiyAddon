package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityRole
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationMemberMenu(
    syncId: Int,
    val community: Community,
    private val playerObject: ServerPlayerEntity,
    playerExecutor: ServerPlayerEntity
) : AbstractMenu(
    syncId,
    menuTitle = generateCommunityMemberListMemberMenuTitle(community, playerObject)
) {

    init {
        addDescriptionButtons()

        val communityRole = community.getMemberRole(playerExecutor.uuid)
        if (communityRole == CommunityRole.OWNER || communityRole == CommunityRole.ADMIN) addManageButtons()
    }

    private fun addDescriptionButtons() {
        addButton(
            slot = 10,
            name = playerObject.name.string,
            itemStack = createPlayerHeadItem(playerObject.name.string, playerObject.uuid)
        ) {}
    }

    private fun addManageButtons() {
        addButton(
            slot = 12,
            name = Translator.tr("ui.community.operation.member.member_page.button.region")?.string ?: "Region Setting",
            item = Items.MAP
        ) {}

        addButton(
            slot = 14,
            name = Translator.tr("ui.community.operation.member.member_page.button.remove")?.string ?: "Remove Member",
            item = Items.ZOMBIE_VILLAGER_SPAWN_EGG
        ) {}

        addButton(
            slot = 19,
            name = Translator.tr("ui.community.operation.member.member_page.button.message")?.string ?: "Send Message",
            item = Items.PAPER
        ) {}

        if (community.getMemberRole(playerObject.uuid) == CommunityRole.OWNER) {
            addButton(
                slot = 21,
                name = Translator.tr("ui.community.operation.member.member_page.button.promote")?.string ?: "Promote",
                item = Items.COMMAND_BLOCK
            ) {}
        }

    }

    companion object {
        fun generateCommunityMemberListMemberMenuTitle(community: Community, playerObject: ServerPlayerEntity): Text {
            return Text.of(
                "${community.getRegion()?.name}" +
                        " - ${playerObject.name.string}" +
                        Translator.tr("ui.community.operation.member.title")
            )
        }
    }
}