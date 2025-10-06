package com.imyvm.community.inter.screen.component

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class ReadOnlySlot(inv: SimpleInventory, index: Int, x: Int, y: Int) : Slot(inv, index, x, y) {
    override fun canTakeItems(player: PlayerEntity) = false
    override fun canInsert(stack: ItemStack) = false
}