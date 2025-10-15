package com.imyvm.community.application.interaction.screen

import com.imyvm.community.inter.screen.CommunityCreationMenu
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.Region
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.AnvilScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object CommunityCreationRenameHandler {
    fun openRenameMenu(
        player: ServerPlayerEntity,
        currentName: String,
        currentShape: Region.Companion.GeoShapeType,
        isManor: Boolean
    ) {
        player.openHandledScreen(object : NamedScreenHandlerFactory {

            override fun createMenu(syncId: Int, inv: PlayerInventory, p: PlayerEntity): ScreenHandler {
                val context = ScreenHandlerContext.create(p.world, p.blockPos)

                val anvil = object : AnvilScreenHandler(syncId, inv, context) {

                    override fun canUse(player: PlayerEntity?) = true

                    override fun onTakeOutput(player: PlayerEntity, stack: ItemStack) {
                        super.onTakeOutput(player, stack)
                        val newName = stack.name.string.trim()
                        reopenCommunityCreation(player as ServerPlayerEntity, newName, currentShape, isManor)
                    }

                    override fun onClosed(player: PlayerEntity) {
                        super.onClosed(player)
                        reopenCommunityCreation(player as ServerPlayerEntity, currentName, currentShape, isManor)
                    }

                    override fun canTakeOutput(player: PlayerEntity, present: Boolean): Boolean = true
                }

                val nameTag = ItemStack(Items.NAME_TAG)
                nameTag.set(DataComponentTypes.CUSTOM_NAME, Text.of(currentName))
                anvil.slots[AnvilScreenHandler.INPUT_1_ID].stack = nameTag
                anvil.slots[AnvilScreenHandler.OUTPUT_ID].stack = ItemStack.EMPTY

                anvil.setNewItemName(currentName)

                return anvil
            }

            override fun getDisplayName(): Text =
                Translator.tr("ui.create.rename.title") ?: Text.literal("Rename Community")
        })
    }

    private fun reopenCommunityCreation(
        player: ServerPlayerEntity,
        newName: String,
        shape: Region.Companion.GeoShapeType,
        isManor: Boolean
    ) {
        CommunityMenuOpener.open(player, null) { newSyncId, _ ->
            CommunityCreationMenu(newSyncId, newName, shape, isManor)
        }
    }
}
