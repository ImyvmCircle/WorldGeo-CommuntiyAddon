package com.imyvm.community.inter.screen.outer_community

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityRole
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity

class CommunityMenu(
    syncId: Int,
    content: Pair<ServerPlayerEntity, Community>
) : AbstractMenu(
    syncId,
    menuTitle = content.second.getRegion()?.let { Translator.tr("ui.community.title", it.name , it.numberID)}
) {
    init {
        val (player, community) = content

        addOwnerHeadButton(player, community)
        addOperationButtonTrail(player, community)
        addDescriptionButton(player, community)
        addInteractionButton()
    }

    private fun addOwnerHeadButton(player: ServerPlayerEntity, community: Community) {
        addButton(
            slot = 10,
            name = community.generateCommunityMark(),
            itemStack = createPlayerHeadItem(community.generateCommunityMark(), player.uuid)
        ) {}
    }

    private fun addOperationButtonTrail(player: ServerPlayerEntity, community: Community) {
        if (community.getMemberRole(player.uuid) == CommunityRole.OWNER ||
            community.getMemberRole(player.uuid) == CommunityRole.ADMIN ){
            addButton(
                slot = 12,
                name = Translator.tr("ui.community.button.interaction.operations")?.string ?: "Community Operations",
                item = Items.ANVIL
            ) {}
        }
    }

    private fun addDescriptionButton(player: ServerPlayerEntity, community: Community){
        addButton(
            slot = 19,
            name = Translator.tr("ui.community.button.description.region")?.string ?: "Description",
            item = Items.BOOKSHELF
        ) {
            community.sendCommunityRegionDescription(player)
            it.closeHandledScreen()
        }

        addButton(
            slot = 20,
            name = Translator.tr("ui.community.button.description.announcement")?.string ?: "Announcement",
            item = Items.MAP
        ) {}


        addButton(
            slot = 22,
            name = Translator.tr("ui.community.button.description.members")?.string ?: "Member",
            item = Items.ARMOR_STAND
        ) {}

        addButton(
            slot = 21,
            name = Translator.tr("ui.community.button.description.assets")?.string ?: "Asset",
            item = Items.GOLD_INGOT
        ) {}
    }

    private fun addInteractionButton(){
        addButton(
            slot = 23,
            name = Translator.tr("ui.community.button.interaction.settings")?.string ?: "Settings",
            item = Items.REDSTONE_TORCH
        ) {}

        addButton(
            slot = 24,
            name = Translator.tr("ui.community.button.interaction.teleport")?.string ?: "Teleport to Community",
            item = Items.ENDER_PEARL
        ) {}

        addButton(
            slot = 25,
            Translator.tr("ui.community.button.interaction.teleport.scope")?.string ?: "Teleportation Scope",
            item = Items.COMPASS
        ) {}

        addButton(
            slot = 28,
            name = Translator.tr("ui.community.button.interaction.chat")?.string ?: "Community Chat",
            item = Items.LECTERN
        ) {}

        addButton(
            slot = 29,
            name = Translator.tr("ui.community.button.interaction.advancement")?.string ?: "Advancement",
            item = Items.WRITABLE_BOOK
        ) {}

        addButton(
            slot = 30,
            name = Translator.tr("ui.community.button.interaction.donate")?.string ?: "Donate to Community",
            item = Items.EMERALD
        ) {}

        addButton(
            slot = 31,
            name = Translator.tr("ui.community.button.interaction.shop")?.string ?: "Community Shop",
            item = Items.CHEST
        ) {}

        addButton(
            slot = 32,
            name = Translator.tr("ui.community.button.interaction.like")?.string ?: "Like Community",
            item = Items.PINK_DYE
        ) {}

        addButton(
            slot = 33,
            name = Translator.tr("ui.community.button.interaction.leave")?.string ?: "Leave Community",
            item = Items.ZOMBIE_VILLAGER_SPAWN_EGG
        ) {}

        addButton(
            slot = 34,
            name = Translator.tr ("ui.community.button.interaction.invite")?.string ?: "Invite Member",
            item = Items.VILLAGER_SPAWN_EGG
        ) {}
    }
}

