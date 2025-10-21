package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.application.interaction.screen.inner_community.CommunityOperationRenameMenuAnvilHandler
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractRenameMenuAnvil
import com.imyvm.iwg.inter.api.RegionDataApi
import net.minecraft.server.network.ServerPlayerEntity

class CommunityOperationRenameMenuAnvil(
    player: ServerPlayerEntity,
    private val community: Community,
    private val renameHandler: CommunityOperationRenameMenuAnvilHandler = CommunityOperationRenameMenuAnvilHandler()
): AbstractRenameMenuAnvil(
    player = player,
    initialName = community.regionNumberId?.let { RegionDataApi.getRegion(it)?.name } ?: "Unknown Name"
) {
    override fun processRenamingByNewName(finalName: String) =
        renameHandler.reopenOperationMenuWithNewName(player = player, community = community)

    override fun getMenuTitle() = renameHandler.getTitle()

}