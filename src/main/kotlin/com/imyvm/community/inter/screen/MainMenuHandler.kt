package com.imyvm.community.inter.screen

import com.imyvm.community.util.Translator
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

class MainMenuHandler(
    syncId: Int,
    playerInventory: net.minecraft.entity.player.PlayerInventory
) : ScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId) {

    private val listText: Text = Translator.tr("ui.main.button.list") ?: Text.literal("List")
    private val createText: Text = Translator.tr("ui.main.button.create") ?: Text.literal("Create")
    private val closeText: Text = Translator.tr("ui.main.button.close") ?: Text.literal("Close")

    private val inventory = SimpleInventory(27)

    init {
        for (i in 0 until 27) {
            inventory.setStack(i, createItem(Text.literal(" "), Items.GRAY_STAINED_GLASS_PANE))
        }

        inventory.setStack(10, createItem(listText, Items.WRITABLE_BOOK))
        inventory.setStack(13, createItem(createText, Items.OAK_DOOR))
        inventory.setStack(26, createItem(closeText, Items.BARRIER))

        for (i in 0 until inventory.size()) {
            addSlot(ReadOnlySlot(inventory, i, 0, 0))
        }
    }

    private fun createItem(name: Text, item: Item): ItemStack {
        val stack = ItemStack(item)
        stack.set(DataComponentTypes.CUSTOM_NAME, name)
        return stack
    }

    class ReadOnlySlot(inv: SimpleInventory, index: Int, x: Int, y: Int) : Slot(inv, index, x, y) {
        override fun canTakeItems(player: PlayerEntity) = false
        override fun canInsert(stack: ItemStack) = false
    }

    override fun canUse(player: PlayerEntity) = true

    override fun onSlotClick(slotIndex: Int, button: Int, actionType: SlotActionType, player: PlayerEntity) {
        super.onSlotClick(slotIndex, button, actionType, player)
        if (slotIndex < 0 || slotIndex >= inventory.size()) return

        val clicked = inventory.getStack(slotIndex)
        val nameComponent = clicked.get(DataComponentTypes.CUSTOM_NAME)
        val itemName = nameComponent?.string ?: return

        when (itemName) {
            closeText.string -> {
                (player as? ServerPlayerEntity)?.closeHandledScreen()
                player.sendMessage(Translator.tr("ui.main.button.close.feedback"))
            }
            listText.string -> {
                player.sendMessage(Text.literal("打开聚落列表（未实现）"))
            }
            createText.string -> {
                player.sendMessage(Text.literal("创建聚落（未实现）"))
            }
        }
    }

    override fun quickMove(player: PlayerEntity, slotIndex: Int): ItemStack = ItemStack.EMPTY
}
