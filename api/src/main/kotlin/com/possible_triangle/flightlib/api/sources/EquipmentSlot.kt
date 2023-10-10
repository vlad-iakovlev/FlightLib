package com.possible_triangle.flightlib.api.sources

import com.possible_triangle.flightlib.api.IJetpack
import com.possible_triangle.flightlib.api.ISource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class EquipmentSource(val slot: EquipmentSlot, val stack: ItemStack) : ISource {

    override fun isDisabled(context: IJetpack.Context): Boolean {
        val entity = context.entity
        return entity is Player && entity.cooldowns.isOnCooldown(stack.item)
    }

}