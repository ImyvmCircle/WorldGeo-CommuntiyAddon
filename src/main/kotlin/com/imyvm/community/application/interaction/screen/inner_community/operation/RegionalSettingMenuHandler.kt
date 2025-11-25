package com.imyvm.community.application.interaction.screen.inner_community.operation

import com.imyvm.community.domain.Community
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.domain.component.PermissionKey
import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity

fun togglePermissionSetting(
    playerExecutor: ServerPlayerEntity,
    community: Community,
    scope: GeoScope?,
    playerObject: GameProfile?,
    permissionKey: PermissionKey
) {
    togglePermissionSettingInRegion(community, scope, playerObject, permissionKey)
    refreshSettingInMenu(playerExecutor, community)
}

private fun togglePermissionSettingInRegion(
    community: Community,
    scope: GeoScope?,
    playerObject: GameProfile?,
    permissionKey: PermissionKey
){

}

private fun refreshSettingInMenu(
    playerExecutor: ServerPlayerEntity,
    community: Community
){

}