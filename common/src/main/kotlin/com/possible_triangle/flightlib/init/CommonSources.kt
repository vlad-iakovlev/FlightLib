package com.possible_triangle.flightlib.init

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.sources.EntitySource
import com.possible_triangle.flightlib.api.sources.EquipmentSource
import com.possible_triangle.flightlib.platform.Services
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Items

object CommonSources {

    fun register() {
        IFlightApi.INSTANCE.addSourceProvider { entity ->
            EquipmentSlot.values().map {
                val stack = entity.getItemBySlot(it)
                stack.item to EquipmentSource(it, stack)
            }
        }

        IFlightApi.INSTANCE.addSourceProvider { entity ->
            listOf(entity to EntitySource)
        }

        if (Services.PLATFORM.isDevelopmentEnvironment) {
            IFlightApi.INSTANCE.addSourceCaster {
                listOf {
                    when (it) {
                        Items.DIAMOND_CHESTPLATE -> DevJetpack
                        Items.SHIELD -> DevJetpack
                        else -> null
                    }
                }
            }
        }
    }

}