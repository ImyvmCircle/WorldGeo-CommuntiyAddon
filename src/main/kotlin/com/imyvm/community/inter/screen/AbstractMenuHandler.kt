package com.imyvm.community.inter.screen

import com.imyvm.community.inter.screen.component.MenuButton
import com.imyvm.community.inter.screen.component.ReadOnlySlot
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

abstract class AbstractMenuHandler(
    syncId: Int,
    rows: Int = 3,
    private val defaultBackground: Item = Items.GRAY_STAINED_GLASS_PANE,
    private val defaultBackgroundName: String = " ",
    val menuTitle: Text = Text.literal("Menu")
) : ScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId) {

    private val inventory = SimpleInventory(rows * 9)
    private val buttons = mutableListOf<MenuButton>()

    init {
        fillBackground()
        setupSlots()
    }

    private fun fillBackground() {
        for (i in 0 until inventory.size()) {
            inventory.setStack(i, createItem(Text.literal(defaultBackgroundName), defaultBackground))
        }
    }

    private fun setupSlots() {
        for (i in 0 until inventory.size()) {
            addSlot(ReadOnlySlot(inventory, i, 0, 0))
        }
    }

    protected fun addButton(slot: Int, name: String, item: Item, onClick: (ServerPlayerEntity) -> Unit) {
        buttons.add(MenuButton(slot, item, name, onClick))
        inventory.setStack(slot, createItem(Text.literal(name), item))
    }

    private fun createItem(name: Text, item: Item): ItemStack {
        val stack = ItemStack(item)
        stack.set(DataComponentTypes.CUSTOM_NAME, name)
        return stack
    }

    override fun onSlotClick(slotIndex: Int, button: Int, actionType: SlotActionType, player: PlayerEntity) {
        super.onSlotClick(slotIndex, button, actionType, player)
        if (slotIndex < 0 || slotIndex >= inventory.size()) return

        (player as? ServerPlayerEntity)?.let { sp ->
            val clickedName = inventory.getStack(slotIndex).get(DataComponentTypes.CUSTOM_NAME)?.string
            buttons.find { it.slot == slotIndex && it.name == clickedName }?.onClick?.invoke(sp)
        }
    }

    override fun canUse(player: PlayerEntity) = true
    override fun quickMove(player: PlayerEntity, slotIndex: Int): ItemStack = ItemStack.EMPTY
}

