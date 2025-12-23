package com.imyvm.community.application.interaction.screen.inner_community.operation

import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.domain.Community
import com.imyvm.community.domain.GeographicFunctionType
import com.imyvm.community.inter.screen.inner_community.operation.RegionalSettingMenu
import com.imyvm.iwg.domain.component.GeoScope
import com.imyvm.iwg.inter.api.PlayerInteractionApi
import com.mojang.authlib.GameProfile
import net.minecraft.server.network.ServerPlayerEntity

fun executeRegion(
    playerExecutor: ServerPlayerEntity,
    community: Community,
    playerObject: GameProfile? = null
) {
    CommunityMenuOpener.open(playerExecutor) { syncId ->
        RegionalSettingMenu(
            syncId = syncId,
            playerExecutor = playerExecutor,
            community = community,
            playerObject = playerObject
        )
    }
}

fun executeScope(
    playerExecutor: ServerPlayerEntity,
    community: Community,
    scope: GeoScope,
    geographicFunctionType: GeographicFunctionType,
    playerObject: GameProfile? = null
) {
    when (geographicFunctionType){
        GeographicFunctionType.GEOMETRY_MODIFICATION -> {
            val communityRegion = community.getRegion()
            communityRegion?.let { PlayerInteractionApi.modifyScope(playerExecutor, it, scope.scopeName) }
            playerExecutor.closeHandledScreen()
        }
        GeographicFunctionType.SETTING_ADJUSTMENT -> {
            CommunityMenuOpener.open(playerExecutor) { syncId ->
                RegionalSettingMenu(
                    syncId = syncId,
                    playerExecutor = playerExecutor,
                    community = community,
                    scope = scope,
                    playerObject = playerObject
                )
            }
        }
        GeographicFunctionType.TELEPORT_POINT_LOCATING -> {

        }
    }
}