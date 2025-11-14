package com.imyvm.community.application.interaction.screen.outer_community

import com.imyvm.community.application.interaction.common.onCreateCommunity
import com.imyvm.community.application.interaction.screen.CommunityMenuOpener
import com.imyvm.community.inter.screen.outer_community.CommunityCreationMenu
import com.imyvm.iwg.domain.component.GeoShapeType
import net.minecraft.server.network.ServerPlayerEntity

fun runRenameNewCommunity(
    player: ServerPlayerEntity,
    currentName: String,
    currentShape: GeoShapeType,
    isManor: Boolean
) {
    CommunityMenuOpener.openCreationRenameAnvilMenu(player, currentName, currentShape, isManor)
}

fun runSwitchCommunityShape(
    player: ServerPlayerEntity,
    communityName: String,
    geoShapeType: GeoShapeType,
    isManor: Boolean
){
    val newType = when(geoShapeType){
        GeoShapeType.CIRCLE -> GeoShapeType.RECTANGLE
        GeoShapeType.RECTANGLE -> GeoShapeType.POLYGON
        GeoShapeType.POLYGON -> GeoShapeType.CIRCLE
        GeoShapeType.UNKNOWN -> GeoShapeType.RECTANGLE
    }

    CommunityMenuOpener.open(player) { syncId ->
        CommunityCreationMenu(syncId, communityName, newType, isManor, player)
    }
}

fun runSwitchCommunityType(
    player: ServerPlayerEntity,
    communityName: String,
    geoShapeType: GeoShapeType,
    isManor: Boolean
){
    CommunityMenuOpener.open(player) { syncId ->
        CommunityCreationMenu(syncId, communityName, geoShapeType, !isManor, player)
    }
}

fun runConfirmCommunityCreation(
    player: ServerPlayerEntity,
    communityName: String,
    geoShapeType: GeoShapeType,
    isManor: Boolean
){
    player.closeHandledScreen()
    onCreateCommunity(
        player,
        if (isManor) "manor" else "realm",
        communityName,
        geoShapeType.toString()
    )
}