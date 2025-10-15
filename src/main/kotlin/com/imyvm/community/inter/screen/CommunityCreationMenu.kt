package com.imyvm.community.inter.screen

import com.imyvm.community.application.interaction.screen.runRenameNewCommunity
import com.imyvm.community.application.interaction.screen.runSwitchCommunityShape
import com.imyvm.community.application.interaction.screen.runSwitchCommunityType
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.Region
import net.minecraft.item.Items
import net.minecraft.text.Text

class CommunityCreationMenu(
    syncId: Int,
    currentName: String? = Translator.tr("ui.create.title").toString(),
    currentShape: Region.Companion.GeoShapeType = Region.Companion.GeoShapeType.RECTANGLE,
    isCurrentCommunityTypeManor: Boolean = true
) : AbstractMenu(
    syncId,
    menuTitle = Text.of(currentName) ?: Translator.tr("ui.create.title")
) {
    init {
        addButton(
            slot = 10,
            name = menuTitle?.string ?: "Name",
            item = Items.NAME_TAG
        ) { runRenameNewCommunity(it, menuTitle?.string ?: "Name", currentShape, isCurrentCommunityTypeManor)  }

        addButton(
            slot = 13,
            name = currentShape.toString(),
            item = when (currentShape) {
                Region.Companion.GeoShapeType.CIRCLE -> Items.CLOCK
                Region.Companion.GeoShapeType.RECTANGLE -> Items.MAP
                Region.Companion.GeoShapeType.POLYGON -> Items.NETHER_STAR
                Region.Companion.GeoShapeType.UNKNOWN -> Items.STRUCTURE_BLOCK
            }
        ) { runSwitchCommunityShape(it, menuTitle.toString(), currentShape, isCurrentCommunityTypeManor) }

        addButton(
            slot = 16,
            name = if (isCurrentCommunityTypeManor) Translator.tr("ui.create.button.type.manor")?.string ?: "Manor"
                else Translator.tr("ui.create.button.type.city")?.string ?: "Realm",
            item = if (isCurrentCommunityTypeManor) Items.BIRCH_PLANKS else Items.CHERRY_PLANKS
        ) { runSwitchCommunityType(it, menuTitle.toString(), currentShape, isCurrentCommunityTypeManor) }
    }
}