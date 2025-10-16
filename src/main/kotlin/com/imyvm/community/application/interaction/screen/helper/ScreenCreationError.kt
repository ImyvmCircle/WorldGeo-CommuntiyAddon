package com.imyvm.community.application.interaction.screen.helper

import com.imyvm.community.infra.CommunityDatabase
import com.imyvm.community.util.Translator
import com.imyvm.economy.EconomyMod
import com.imyvm.iwg.ImyvmWorldGeo
import com.imyvm.iwg.domain.Region
import net.minecraft.server.network.ServerPlayerEntity

fun generateCreationError(
    currentName: String,
    currentShape: Region.Companion.GeoShapeType,
    isCurrentCommunityTypeManor: Boolean,
    playerEntity: ServerPlayerEntity
): String {
    val errors = mutableListOf<String>()
    if (currentName.isBlank()) {
        errors.add(Translator.tr("ui.create.error.name_empty")?.string ?: "NameEmpty")
    } else if (CommunityDatabase.communities.any { it.getRegion()?.name == currentName }) {
        errors.add(Translator.tr("ui.create.error.name_duplicated")?.string ?: "NameDuplicated")
    } else if (currentShape == Region.Companion.GeoShapeType.UNKNOWN) {
        errors.add(Translator.tr("ui.create.error.shape_unknown")?.string ?: "ShapeUnknown")
    } else if (currentShape == Region.Companion.GeoShapeType.POLYGON) {
        val points = ImyvmWorldGeo.pointSelectingPlayers[playerEntity.uuid]
        if (points == null || points.size < 3) {
            errors.add(
                Translator.tr("ui.create.error.shape_polygon")?.string
                    ?: "PolygonNeed3Points+Selected"
            )
        }
    } else if (currentShape == Region.Companion.GeoShapeType.CIRCLE || currentShape == Region.Companion.GeoShapeType.RECTANGLE) {
        val points = ImyvmWorldGeo.pointSelectingPlayers[playerEntity.uuid]
        if (points == null || points.size < 2) {
            errors.add(
                Translator.tr("ui.create.error.shape_not_enough")?.string
                    ?: "Need2Points+Selected"
            )
        }
    } else if (isCurrentCommunityTypeManor && EconomyMod.data.getOrCreate(playerEntity).money < com.imyvm.community.infra.CommunityConfig.PRICE_MANOR.value) {
        errors.add(
            Translator.tr("ui.create.error.money_manor")?.string
                ?: "NotEnoughMoneyManor"
        )
    } else if (!isCurrentCommunityTypeManor && EconomyMod.data.getOrCreate(playerEntity).money < com.imyvm.community.infra.CommunityConfig.PRICE_REALM.value) {
        errors.add(
            Translator.tr("ui.create.error.money_realm")?.string
                ?: "NotEnoughMoneyRealm"
        )
    }

    return if (errors.isEmpty()) "" else " ERRORS:" + errors.joinToString(";")
}