package com.imyvm.community.application

import com.imyvm.community.CommunityDatabase.Companion.communities
import com.imyvm.community.Translator
import com.imyvm.community.domain.Community
import com.imyvm.iwg.ImyvmWorldGeo
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

fun getCommunityByName(context: CommandContext<ServerCommandSource>): Community? {
    val player = context.source.player ?: return null

    val name = StringArgumentType.getString(context, "name")
    val targetRegion = ImyvmWorldGeo.data.getRegionList().find {
        it.name.equals(name, ignoreCase = true)
    } ?: run {
        player.sendMessage(Translator.tr("community.notfound.name", name))
        return null
    }
    val targetCommunity = communities.find {
        it.regionNumberId == targetRegion.numberID
    } ?:run {
        player.sendMessage(Translator.tr("community.notfound.name", name))
        return null
    }

    return targetCommunity
}

fun getCommunityById(context: CommandContext<ServerCommandSource>): Community? {
    val player = context.source.player ?: return null

    val id = IntegerArgumentType.getInteger(context, "id")
    val targetCommunity = communities.find {
        it.id == id
    } ?: run {
        player.sendMessage(Translator.tr("community.notfound.id", id))
        return null
    }
    return targetCommunity
}
