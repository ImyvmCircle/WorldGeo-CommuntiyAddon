package com.imyvm.community.application.interaction.screen

import com.imyvm.community.inter.screen.CommunityListMenu
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

fun runList(player: ServerPlayerEntity){
    CommunityMenuOpener.open(player, "ALL") { syncId, context -> CommunityListMenu(syncId, context) }
}

fun runCreate(player: ServerPlayerEntity){
    player.sendMessage(Text.literal("创建聚落（未实现）"))
}

fun runMyCommunity(player: ServerPlayerEntity){
    player.sendMessage(Text.literal("打开我的聚落（未实现）"))
}