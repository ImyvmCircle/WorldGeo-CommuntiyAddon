package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractRenameMenuAnvil
import com.imyvm.community.util.Translator.tr
import com.imyvm.community.util.Translator.trMenu
import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityNotificationMenuAnvil(
    private val playerExecutor: ServerPlayerEntity,
    initialName: String,
    private val playerObject: GameProfile,
    val community: Community
): AbstractRenameMenuAnvil(
    playerExecutor,
    initialName
) {
    override fun processRenaming(finalName: String) {
        if (finalName.isBlank()) {
            trMenu(
                playerExecutor,
                "community.operation.member.message.sent.fail.empty",
                playerObject.name
            )
            return
        } else if (community.member[playerObject.id] == null) {
            trMenu(
                playerExecutor,
                "community.operation.member.message.sent.fail.not_member",
                playerObject.name
            )
            return
        } else {
            val message = tr(
                "community.operation.member.message.received.content",
                playerExecutor.name.string,
                finalName
            )
            if (message != null && message.string != "") {
                community.member[playerObject.id]!!.communityMail.add(message)
                trMenu(
                    playerExecutor,
                    "community.operation.member.message.sent",
                    playerObject.name
                )
            } else {
                trMenu(
                    playerExecutor,
                    "community.operation.member.message.sent.fail.empty",
                    playerObject.name
                )
            }

        }
    }

    override fun getMenuTitle(): Text  = Text.of("(Edit your notification here to ${playerObject.name})")
}