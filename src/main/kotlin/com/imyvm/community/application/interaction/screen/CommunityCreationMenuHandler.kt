package com.imyvm.community.application.interaction.screen

import com.imyvm.community.application.interaction.common.onCreateCommunity
import com.imyvm.community.inter.screen.CommunityCreationMenu
import com.imyvm.iwg.domain.Region
import net.minecraft.server.network.ServerPlayerEntity

fun runRenameNewCommunity(
    player: ServerPlayerEntity,
    currentName: String,
    currentShape: Region.Companion.GeoShapeType,
    isManor: Boolean
) {
    CommunityMenuOpener.openCreationRenameAnvilMenu(player, currentName, currentShape, isManor)
}

fun runSwitchCommunityShape(
    player: ServerPlayerEntity,
    communityName: String,
    geoShapeType: Region.Companion.GeoShapeType,
    isManor: Boolean
){
    val newType = when(geoShapeType){
        Region.Companion.GeoShapeType.CIRCLE -> Region.Companion.GeoShapeType.RECTANGLE
        Region.Companion.GeoShapeType.RECTANGLE -> Region.Companion.GeoShapeType.POLYGON
        Region.Companion.GeoShapeType.POLYGON -> Region.Companion.GeoShapeType.CIRCLE
        Region.Companion.GeoShapeType.UNKNOWN -> Region.Companion.GeoShapeType.RECTANGLE
    }

    CommunityMenuOpener.open(player, null) { syncId, _ ->
        CommunityCreationMenu(syncId, communityName, newType, isManor, player)
    }
}

fun runSwitchCommunityType(
    player: ServerPlayerEntity,
    communityName: String,
    geoShapeType: Region.Companion.GeoShapeType,
    isManor: Boolean
){
    CommunityMenuOpener.open(player, null) { syncId, _ ->
        CommunityCreationMenu(syncId, communityName, geoShapeType, !isManor, player)
    }
}

fun runConfirmCommunityCreation(
    player: ServerPlayerEntity,
    communityName: String,
    geoShapeType: Region.Companion.GeoShapeType,
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