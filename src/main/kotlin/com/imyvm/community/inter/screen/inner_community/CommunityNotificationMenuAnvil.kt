package com.imyvm.community.inter.screen.inner_community

import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.AbstractRenameMenuAnvil
import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityNotificationMenuAnvil(
    playerExecutor: ServerPlayerEntity,
    initialName: String,
    val playerObect: GameProfile,
    val community: Community
): AbstractRenameMenuAnvil(
    playerExecutor,
    initialName
) {
    override fun processRenaming(finalName: String) {
        TODO("Not yet implemented")
    }

    override fun getMenuTitle(): Text  = Text.of("(Edit your notification here to ${playerObect.name})")
}