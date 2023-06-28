package com.possible_triangle.flightlib.init

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.IJetpack
import com.possible_triangle.flightlib.api.ISource
import com.possible_triangle.flightlib.api.sources.EntitySource
import com.possible_triangle.flightlib.api.sources.EquipmentSource
import com.possible_triangle.flightlib.platform.Services
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object CommonSources {

    fun addEquipmentSources(supplier: (ItemStack) -> IJetpack?) {
        IFlightApi.INSTANCE.addSource { entity ->
            EquipmentSlot.values().map {
                val stack = entity.getItemBySlot(it)
                ISource.ProviderEntry(EquipmentSource(it)) { supplier(stack) }
            }
        }
    }

    fun addEntitySources(supplier: (LivingEntity) -> IJetpack?) {
        IFlightApi.INSTANCE.addSource { entity ->
            listOf(ISource.ProviderEntry(EntitySource) { supplier(entity) })
        }
    }

    fun register() {
        if (Services.PLATFORM.isDevelopmentEnvironment) {
            addEquipmentSources {
                if (it.`is`(Items.DIAMOND_CHESTPLATE)) DevJetpack
                else null
            }
        }
    }

}