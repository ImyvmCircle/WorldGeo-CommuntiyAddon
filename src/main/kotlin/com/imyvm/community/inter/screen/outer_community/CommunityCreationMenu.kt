package com.imyvm.community.inter.screen.outer_community

import com.imyvm.community.application.interaction.screen.helper.generateCreationError
import com.imyvm.community.application.interaction.screen.outer_community.runConfirmCommunityCreation
import com.imyvm.community.application.interaction.screen.outer_community.runRenameNewCommunity
import com.imyvm.community.application.interaction.screen.outer_community.runSwitchCommunityShape
import com.imyvm.community.application.interaction.screen.outer_community.runSwitchCommunityType
import com.imyvm.community.inter.screen.AbstractMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.Region
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class CommunityCreationMenu(
    syncId: Int,
    currentName: String = Translator.tr("ui.create.title")?.string ?: "New-Community",
    currentShape: Region.Companion.GeoShapeType = Region.Companion.GeoShapeType.RECTANGLE,
    isCurrentCommunityTypeManor: Boolean = true,
    playerEntity: ServerPlayerEntity
) : AbstractMenu(
    syncId,
    menuTitle = (Text.of(currentName
            + generateCreationError(currentName, currentShape, isCurrentCommunityTypeManor, playerEntity))
        ?: Translator.tr("ui.create.title",
            generateCreationError(currentName, currentShape, isCurrentCommunityTypeManor, playerEntity)))
) {
    init {
        addButton(
            slot = 10,
            name = currentName,
            item = Items.NAME_TAG
        ) { runRenameNewCommunity(it, currentName, currentShape, isCurrentCommunityTypeManor) }

        addButton(
            slot = 13,
            name = (Translator.tr("ui.create.button.shape.prefix")?.string ?: "Current Shape(Click to change):")
                    + currentShape.toString(),
            item = when (currentShape) {
                Region.Companion.GeoShapeType.CIRCLE -> Items.CLOCK
                Region.Companion.GeoShapeType.RECTANGLE -> Items.MAP
                Region.Companion.GeoShapeType.POLYGON -> Items.NETHER_STAR
                Region.Companion.GeoShapeType.UNKNOWN -> Items.STRUCTURE_BLOCK
            }
        ) { runSwitchCommunityShape(it, currentName, currentShape, isCurrentCommunityTypeManor) }

        addButton(
            slot = 16,
            name = if (isCurrentCommunityTypeManor) Translator.tr("ui.create.button.type.manor")?.string ?: "Manor"
                else Translator.tr("ui.create.button.type.realm")?.string ?: "Realm",
            item = if (isCurrentCommunityTypeManor) Items.BIRCH_PLANKS else Items.CHERRY_PLANKS
        ) { runSwitchCommunityType(it, currentName, currentShape, isCurrentCommunityTypeManor) }

        addButton(
            slot = 35,
            name = Translator.tr("ui.create.button.confirm")?.string ?: "Confirm Creation",
            item = Items.EMERALD_BLOCK
        ) { runConfirmCommunityCreation(it, currentName, currentShape, isCurrentCommunityTypeManor) }
    }
}