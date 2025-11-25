package com.imyvm.community.application.interaction.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.inter.screen.inner_community.operation.RegionalSettingMenu
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.domain.component.PermissionKey
import com.imyvm.iwg.domain.component.SettingTypes
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import com.imyvm.iwg.inter.api.RegionDataApi
import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity

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
    playerObject: GameProfile?,
    permissionKey: PermissionKey
){
    val region = community.getRegion() ?: return
    if (scope == null) {
        val currentPermissionSet = RegionDataApi.getRegionGlobalSettingsByType(region, SettingTypes.PERMISSION)
        for (settingItem in currentPermissionSet) {
            if ((settingItem.key == permissionKey) && ((!settingItem.isPersonal && (playerObject == null)) || ((playerObject != null) && (settingItem.playerUUID == playerObject.id)))) {
                val newValue = settingItem.value == true
                PlayerInteractionApi.removeSettingRegion(playerExecutor, region, permissionKey.toString(),
                    playerObject?.id?.toString()
                )
                PlayerInteractionApi.addSettingRegion(playerExecutor, region, permissionKey.toString(),
                    newValue.toString(), playerObject?.id?.toString()
                )
            } else {
                PlayerInteractionApi.addSettingRegion(playerExecutor, region, permissionKey.toString(),
                    false.toString(), playerObject?.id?.toString()
                )
            }
        }
    } else {
        val currentPermissionSet = RegionDataApi.getScopeGlobalSettingsByType(scope, SettingTypes.PERMISSION)
        for (settingItem in currentPermissionSet) {
            if ((settingItem.key == permissionKey) && ((!settingItem.isPersonal && (playerObject == null)) || ((playerObject != null) && (settingItem.playerUUID == playerObject.id)))) {
                val newValue = settingItem.value == true
                PlayerInteractionApi.removeSettingScope(playerExecutor, region, scope.scopeName, permissionKey.toString(),
                    playerObject?.id?.toString()
                )
                PlayerInteractionApi.addSettingScope(playerExecutor, region, scope.scopeName, permissionKey.toString(),
                    newValue.toString(), playerObject?.id?.toString()
                )
            } else {
                PlayerInteractionApi.addSettingScope(playerExecutor, region, scope.scopeName, permissionKey.toString(),
                    false.toString(), playerObject?.id?.toString()
                )
            }
        }
    }
}

private fun refreshSettingInMenu(
    playerExecutor: ServerPlayerEntity,
    community: Community,
    scope: GeoScope?,
    playerProfile: GameProfile?
){
    CommunityMenuOpener.open(playerExecutor) { syncId ->
        RegionalSettingMenu(syncId, playerExecutor, community, scope, playerProfile)
    }
}