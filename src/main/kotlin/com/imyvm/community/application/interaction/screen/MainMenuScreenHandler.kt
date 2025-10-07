package com.imyvm.community.application.interaction.screen

import com.imyvm.community.util.Translator
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

fun runList(player: ServerPlayerEntity){
    player.sendMessage(Text.literal("打开聚落列表（未实现）"))
}

fun runCreate(player: ServerPlayerEntity){
    player.sendMessage(Text.literal("创建聚落（未实现）"))
}

fun runMyCommunity(player: ServerPlayerEntity){
    player.sendMessage(Text.literal("打开我的聚落（未实现）"))
}

fun runClose(player: ServerPlayerEntity){
    player.closeHandledScreen()
    player.sendMessage(Translator.tr("ui.main.button.close.feedback"))
}