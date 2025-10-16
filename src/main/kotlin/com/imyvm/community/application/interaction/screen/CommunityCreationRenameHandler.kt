package com.imyvm.community.application.interaction.screen

import com.imyvm.community.inter.screen.CommunityCreationMenu
import com.imyvm.community.inter.screen.component.ReadOnlySlot
import com.imyvm.community.util.Translator
import com.imyvm.iwg.domain.Region
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.AnvilScreenHandler
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object CommunityCreationRenameHandler {

    private var isReopened = false

    fun openRenameMenu(
        player: ServerPlayerEntity,
        currentName: String,
        currentShape: Region.Companion.GeoShapeType,
        isManor: Boolean
    ) {
        player.openHandledScreen(object : NamedScreenHandlerFactory {

            override fun createMenu(syncId: Int, inv: PlayerInventory, p: PlayerEntity): ScreenHandler {
                val context = ScreenHandlerContext.create(p.world, p.blockPos)

                val simpleInventory = SimpleInventory(3)
                val anvil = object : AnvilScreenHandler(syncId, inv, context) {

                    private var capturedName: String = currentName

                    init {
                        this.slots[INPUT_1_ID] = ReadOnlySlot(simpleInventory, INPUT_1_ID, 27, 47)
                        this.slots[INPUT_2_ID] = ReadOnlySlot(simpleInventory, INPUT_2_ID, 76, 47)
                        this.slots[OUTPUT_ID] = ReadOnlySlot(simpleInventory, OUTPUT_ID, 125, 47)
                    }

                    override fun canUse(player: PlayerEntity?) = true

                    override fun setNewItemName(name: String?): Boolean {
                        capturedName = name ?: currentName
                        return super.setNewItemName(name)
                    }

                    override fun onSlotClick(slotIndex: Int, button: Int, actionType: SlotActionType, player: PlayerEntity) {
                        if (slotIndex == OUTPUT_ID) {
                            (player as ServerPlayerEntity).closeHandledScreen()
                        }
                    }

                    override fun onClosed(player: PlayerEntity) {
                        val newName = capturedName.trim()
                        if (!isReopened){
                            reopenCommunityCreation(player as ServerPlayerEntity, newName, currentShape, isManor)
                        }
                    }
                }

                val nameTag = ItemStack(Items.NAME_TAG)
                nameTag.set(DataComponentTypes.CUSTOM_NAME, Text.of(currentName))
                anvil.slots[AnvilScreenHandler.INPUT_1_ID].stack = nameTag

                anvil.setNewItemName(currentName)
                anvil.updateResult()

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
        isReopened = true
        CommunityMenuOpener.open(player, null) { newSyncId, _ ->
            CommunityCreationMenu(newSyncId, newName, shape, isManor)
        }
    }

    fun resetReopenedFlag() {
        isReopened = false
    }
}