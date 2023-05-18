package com.possible_triangle.flightlib.init

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.ISource
import com.possible_triangle.flightlib.api.sources.EquipmentSource
import com.possible_triangle.flightlib.platform.Services
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Items

object CommonSources {

    fun register() {
        if (Services.PLATFORM.isDevelopmentEnvironment) {
            IFlightApi.INSTANCE.addSource { entity ->
                val items = listOf(entity.getItemBySlot(EquipmentSlot.CHEST))
                items.filter { it.`is`(Items.DIAMOND_CHESTPLATE) }.map {
                    ISource.ProviderEntry(EquipmentSource(EquipmentSlot.CHEST)) { DevJetpack }
                }
            }
        }
    }

}