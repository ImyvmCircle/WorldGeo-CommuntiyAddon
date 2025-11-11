package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractRenameMenuAnvil
import com.imyvm.community.util.Translator.tr
import com.imyvm.community.util.Translator.trMenu
import com.imyvm.community.util.getFormattedMillsHour
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
        if (!checkPrerequisites(finalName)) return
        val member = community.member[playerObject.id]!!

        val messageSent = constructAndSendMail(member.mail, finalName)
        if (messageSent) {
            trMenu(playerExecutor, "community.operation.member.message.sent", playerObject.name)
        } else {
            trMenu(playerExecutor, "community.operation.member.message.sent.error.empty", playerObject.name)
        }
    }

    override fun getMenuTitle(): Text  = Text.of("(Edit your notification here to ${playerObject.name})")

    private fun checkPrerequisites(finalName: String): Boolean {
        if (finalName.isBlank()) {
            trMenu(playerExecutor, "community.operation.member.message.sent.error.empty", playerObject.name)
            return false
        }
        if (community.member[playerObject.id] == null) {
            trMenu(playerExecutor, "community.operation.member.message.sent.error.not_member", playerObject.name)
            return false
        }
        return true
    }

    private fun constructAndSendMail(mailBox: MutableList<Text>, finalName: String): Boolean {
        val formattedTime = getFormattedMillsHour(System.currentTimeMillis())
        val regionName = community.getRegion()?.name ?: "Community#${community.regionNumberId}"

        val message = tr(
            "mail.notification.community.message",
            formattedTime,
            regionName,
            playerExecutor.name.string,
            finalName
        )

        if (message != null && message.string.isNotEmpty()) {
            mailBox.add(message)
            return true
        }
        return false
    }

}