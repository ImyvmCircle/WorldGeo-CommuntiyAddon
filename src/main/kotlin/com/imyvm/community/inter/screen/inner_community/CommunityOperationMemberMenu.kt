package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.application.interaction.screen.inner_community.runNotifyMember
import com.imyvm.community.application.interaction.screen.inner_community.runOpenPlayerRegionalSettings
import com.imyvm.community.application.interaction.screen.inner_community.runPromoteMember
import com.imyvm.community.application.interaction.screen.inner_community.runRemoveMember
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityRole
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import com.mojang.authlib.GameProfile
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationMemberMenu(
    syncId: Int,
    val community: Community,
    private val playerObject: GameProfile,
    private val playerExecutor: ServerPlayerEntity
) : AbstractMenu(
    syncId,
    menuTitle = generateCommunityMemberListMemberMenuTitle(community, playerObject)
) {

    init {
        addDescriptionButtons()
        if (isManageableMemberRole(community, playerObject, playerExecutor)) addManageButtons()
    }

    private fun addDescriptionButtons() {
        addButton(
            slot = 10,
            name = playerObject.name,
            itemStack = createPlayerHeadItem(playerObject.name, playerObject.id)
        ) {}
    }

    private fun addManageButtons() {
        addButton(
            slot = 12,
            name = Translator.tr("ui.community.operation.member.member_page.button.region")?.string ?: "Region Setting",
            item = Items.MAP
        ) { runOpenPlayerRegionalSettings(community, playerObject, playerExecutor) }

        addButton(
            slot = 14,
            name = Translator.tr("ui.community.operation.member.member_page.button.remove")?.string ?: "Remove Member",
            item = Items.ZOMBIE_VILLAGER_SPAWN_EGG
        ) { runRemoveMember() }

        addButton(
            slot = 19,
            name = Translator.tr("ui.community.operation.member.member_page.button.message")?.string ?: "Send Message",
            item = Items.PAPER
        ) { runNotifyMember() }

        if (community.getMemberRole(playerObject.id) == CommunityRole.OWNER) {
            addButton(
                slot = 21,
                name = Translator.tr("ui.community.operation.member.member_page.button.promote")?.string ?: "Promote",
                item = Items.COMMAND_BLOCK
            ) { runPromoteMember() }
        }

    }

    companion object {
        fun isManageableMemberRole(community: Community, playerObject: GameProfile, playerExecutor: ServerPlayerEntity): Boolean {
            return when (community.getMemberRole(playerObject.id)) {
                CommunityRole.OWNER -> false
                CommunityRole.ADMIN -> community.getMemberRole(playerExecutor.uuid) == CommunityRole.OWNER
                CommunityRole.MEMBER -> {
                    val executorRole = community.getMemberRole(playerExecutor.uuid)
                    executorRole == CommunityRole.OWNER || executorRole == CommunityRole.ADMIN
                }
                CommunityRole.APPLICANT -> false
                null -> false
            }
        }

        fun generateCommunityMemberListMemberMenuTitle(community: Community, playerObject: GameProfile): Text {
            return Text.of(
                "${community.getRegion()?.name}" +
                        " - ${playerObject.name}" +
                        Translator.tr("ui.community.operation.member.title")
            )
        }
    }
}