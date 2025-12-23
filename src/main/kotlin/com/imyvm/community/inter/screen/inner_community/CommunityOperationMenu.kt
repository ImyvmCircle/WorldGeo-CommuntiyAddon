package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.application.interaction.screen.inner_community.*
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.GeographicFunctionType
import com.imyvm.community.domain.community.CommunityJoinPolicy
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationMenu(
    syncId: Int,
    community: Community,
    player: ServerPlayerEntity
): AbstractMenu(
    syncId,
    menuTitle = Text.of(
        community.generateCommunityMark()
                + " - " + Translator.tr("ui.community.operation.title")?.string
                + ":" + player.name.string
    )
){
    init {
        addStaticButtons(player, community)
        addChangeableButtons(player, community)
    }

    private fun addStaticButtons(player: ServerPlayerEntity, community: Community) {
        addButton(
            slot = 10,
            name = Translator.tr("ui.community.operation.button.name")?.string ?: "Community Name",
            item = Items.NAME_TAG
        ) { runOPRenameCommunity(player, community) }

        addButton(
            slot = 11,
            name = Translator.tr("ui.community.operation.button.members")?.string ?: "Manage Members",
            item = Items.PLAYER_HEAD
        ) { runOpManageMembers(player, community) }

        addButton(
            slot = 12,
            name = Translator.tr("ui.community.operation.button.audit")?.string ?: "Community Audit",
            item = Items.REDSTONE_TORCH
        ){ runOPAuditRequests(player, community) }

        addButton(
            slot = 13,
            name = Translator.tr("ui.community.operation.button.announcement")?.string ?: "Announcement",
            item = Items.PAPER
        ) {}

        addButton(
            slot = 14,
            name = Translator.tr("ui.community.operation.button.advancement")?.string ?: "Advancement",
            item = Items.ITEM_FRAME
        ) { runOPAdvancement(player, community) }

        addButton(
            slot = 15,
            name = Translator.tr("ui.community.operation.button.assets")?.string ?: "Assets",
            item = Items.EMERALD_ORE
        ) {}

        addButton(
            slot = 19,
            name = Translator.tr("ui.community.operation.button.region.geometry")?.string ?: "Region Geometry Modification",
            item = Items.MAP
        ){ runOPRegion(player, community, geographicFunctionType = GeographicFunctionType.GEOMETRY_MODIFICATION) }

        addButton(
            slot = 20,
            name = Translator.tr("ui.community.operation.button.region.setting")?.string ?: "Region Settings",
            item = Items.HEART_OF_THE_SEA
        ){ runOPRegion(player, community, geographicFunctionType = GeographicFunctionType.SETTING_ADJUSTMENT) }

        addButton(
            slot = 21,
            name = Translator.tr("ui.community.operation.button.teleport")?.string ?: "Teleport Point Management",
            item = Items.ENDER_PEARL
        ) { runOPRegion(player, community, geographicFunctionType = GeographicFunctionType.TELEPORT_POINT_LOCATING)}
    }

    private fun addChangeableButtons(player: ServerPlayerEntity, community: Community) {
        addButton(
            slot = 28,
            name = (Translator.tr("ui.community.operation.button.join_policy")?.string
                ?: "Join Policy: ") + community.joinPolicy.toString(),
            item = when (community.joinPolicy) {
                CommunityJoinPolicy.OPEN -> Items.GREEN_WOOL
                CommunityJoinPolicy.APPLICATION -> Items.YELLOW_WOOL
                CommunityJoinPolicy.INVITE_ONLY -> Items.RED_WOOL
            }
        ) { runOPChangeJoinPolicy(player, community, community.joinPolicy) }
    }
}