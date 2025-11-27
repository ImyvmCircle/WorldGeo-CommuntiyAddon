package com.imyvm.community.application.interaction.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.operation.RegionalSettingMenu
import com.imyvm.iwg.domain.Region
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.domain.component.PermissionKey
import com.imyvm.iwg.domain.component.SettingTypes
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import com.imyvm.iwg.inter.api.RegionDataApi
import com.mojang.authlib.GameProfile
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

fun getPermissionButtonItemStack(
    item: Item,
    community: Community,
    scope: GeoScope?,
    playerObject: GameProfile?,
    permissionKey: PermissionKey
): ItemStack {
    TODO()
}

fun togglePermissionSetting(
    playerExecutor: ServerPlayerEntity,
    community: Community,
    scope: GeoScope?,
    playerObject: GameProfile?,
    permissionKey: PermissionKey
) {
    togglePermissionSettingInRegion(playerExecutor, community, scope, playerObject, permissionKey)
    refreshSettingInMenu(playerExecutor, community, scope, playerObject)
}

private fun togglePermissionSettingInRegion(
    playerExecutor: ServerPlayerEntity,
    community: Community,
    scope: GeoScope?,
    targetPlayer: GameProfile?,
    permissionKey: PermissionKey
) {
    val region = community.getRegion() ?: return
    val targetPlayerId = targetPlayer?.id

    val newValueStr = getNewPermissionValue(region, scope, targetPlayerId, permissionKey) ?: "false" // Temporary

    val permissionKeyStr = permissionKey.toString()
    val targetPlayerIdStr = targetPlayerId?.toString()

    if (scope == null) {
        setRegionSetting(
            playerExecutor, region, permissionKeyStr, newValueStr, targetPlayerIdStr
        )
    } else {
        setScopeSetting(
            playerExecutor, region, scope.scopeName, permissionKeyStr, newValueStr, targetPlayerIdStr
        )
    }
}

private fun refreshSettingInMenu(
    playerExecutor: ServerPlayerEntity,
    community: Community,
    scope: GeoScope?,
    playerProfile: GameProfile?
) {
    CommunityMenuOpener.open(playerExecutor) { syncId ->
        RegionalSettingMenu(syncId, playerExecutor, community, scope, playerProfile)
    }
}

private fun getNewPermissionValue(
    region: Region,
    scope: GeoScope?,
    targetPlayerId: UUID?,
    permissionKey: PermissionKey
): String? {
    val currentPermissionSet = if (scope == null) {
        RegionDataApi.getRegionGlobalSettingsByType(region, SettingTypes.PERMISSION)
    } else {
        RegionDataApi.getScopeGlobalSettingsByType(scope, SettingTypes.PERMISSION)
    }

    val existingSetting = currentPermissionSet.find { setting ->
        setting.key == permissionKey && setting.playerUUID == targetPlayerId
    }

    return existingSetting?.value?.toString()?.lowercase()
}

private fun setRegionSetting(
    playerExecutor: ServerPlayerEntity,
    region: Region,
    permissionKeyStr: String,
    newValueStr: String,
    targetPlayerIdStr: String?
) {
    PlayerInteractionApi.removeSettingRegion(
        playerExecutor, region, permissionKeyStr, targetPlayerIdStr
    )
    PlayerInteractionApi.addSettingRegion(
        playerExecutor, region, permissionKeyStr, newValueStr, targetPlayerIdStr
    )
}

private fun setScopeSetting(
    playerExecutor: ServerPlayerEntity,
    region: Region,
    scopeName: String,
    permissionKeyStr: String,
    newValueStr: String,
    targetPlayerIdStr: String?
) {
    PlayerInteractionApi.removeSettingScope(
        playerExecutor, region, scopeName, permissionKeyStr, targetPlayerIdStr
    )
    PlayerInteractionApi.addSettingScope(
        playerExecutor, region, scopeName, permissionKeyStr, newValueStr, targetPlayerIdStr
    )
}