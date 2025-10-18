package com.imyvm.community.inter.screen.component

import com.mojang.authlib.properties.PropertyMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ProfileComponent
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import java.util.*

fun createPlayerHeadItem(name: String, uuid: UUID): ItemStack {
    val headStack = ItemStack(Items.PLAYER_HEAD)
    headStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(name))

    val profileComponent = ProfileComponent(Optional.empty(), Optional.of(uuid), PropertyMap())
    profileComponent.future.thenAccept { fullProfile ->
        headStack.set(DataComponentTypes.PROFILE, fullProfile)
    }

    return headStack
}
