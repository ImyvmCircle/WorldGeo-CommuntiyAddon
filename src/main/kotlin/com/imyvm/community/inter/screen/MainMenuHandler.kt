package com.imyvm.community.inter.screen

import com.imyvm.iwg.application.ui.text.Translator
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.component.DataComponentTypes

class MainMenuHandler(syncId: Int, playerInventory: net.minecraft.entity.player.PlayerInventory)
    : ScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId) {

    private val listStr = Translator.tr("ui.main.button.list")?.string
    private val createStr = Translator.tr("ui.main.button.create")?.string
    private val closeStr = Translator.tr("ui.main.button.close")?.string

    private val inventory = SimpleInventory(27)

    init {
        for (i in 0 until 27) {
            inventory.setStack(i, createItem(" ", Items.GRAY_STAINED_GLASS_PANE))
        }

        inventory.setStack(10, listStr?.let { createItem(it, Items.WRITABLE_BOOK) })
        inventory.setStack(13, createStr?.let { createItem(it, Items.OAK_DOOR) })
        inventory.setStack(26, closeStr?.let { createItem(it, Items.BARRIER) })

        for (i in 0 until inventory.size()) {
            addSlot(Slot(inventory, i, 0, 0))
        }
    }

    private fun createItem(name: String, item: Item): ItemStack {
        val stack = ItemStack(item)
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(name))
        return stack
    }

    override fun canUse(player: PlayerEntity) = true

    override fun onSlotClick(slotIndex: Int, button: Int, actionType: SlotActionType, player: PlayerEntity) {
        super.onSlotClick(slotIndex, button, actionType, player)
        if (slotIndex < 0 || slotIndex >= inventory.size()) return

        val clicked = inventory.getStack(slotIndex)
        val nameComponent = clicked.get(DataComponentTypes.CUSTOM_NAME)
        val itemName = nameComponent?.string ?: return

        when (itemName) {
            closeStr -> {
                (player as? ServerPlayerEntity)?.closeHandledScreen()
                player.sendMessage(Translator.tr("ui.main.button.close.feedback"))
            }
            listStr -> {
                player.sendMessage(Text.literal("打开聚落列表（未实现）"))
            }
            createStr -> {
                player.sendMessage(Text.literal("创建聚落（未实现）"))
            }
        }
    }

    override fun quickMove(player: PlayerEntity, slotIndex: Int): ItemStack = ItemStack.EMPTY
}
