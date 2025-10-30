package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.domain.community.CommunityRole
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.inter.screen.component.createPlayerHeadItem
import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityOperationMemberListMemberMenu(
    syncId: Int,
    community: Community,
    playerObject: ServerPlayerEntity,
    playerExecutor: ServerPlayerEntity
) : AbstractMenu(
    syncId,
    menuTitle = generateCommunityMemberListMemberMenuTitle(community, playerObject)
){

    init {
        addButton(
            slot = 10,
            name = playerObject.name.string,
            itemStack = createPlayerHeadItem(playerObject.name.string, playerObject.uuid)
        ) {}

        addDescriptionButtons()

        val communityRole = community.getMemberRole(playerExecutor.uuid)
        if (communityRole == CommunityRole.OWNER || communityRole == CommunityRole.ADMIN) {
            addManageButtons()
        }
    }

    private fun addDescriptionButtons(){

    }

    private fun addManageButtons(){

    }

    companion object {
        fun generateCommunityMemberListMemberMenuTitle(community: Community, playerObject: ServerPlayerEntity): Text {
            return Text.of("${community.getRegion()?.name}" +
                    " - ${playerObject.name.string}" +
                    Translator.tr("ui.community.operation.member.member_page.title"))
        }
    }
}