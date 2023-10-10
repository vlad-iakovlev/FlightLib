package com.possible_triangle.flightlib.fabric.compat

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.ISource
import com.possible_triangle.flightlib.api.sources.TrinketsSource
import com.possible_triangle.flightlib.platform.Services
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item

object TrinketsCompat {

    fun register() {
        if (!Services.PLATFORM.isModLoaded("trinkets")) return

        IFlightApi.INSTANCE.addSourceProvider(::getTrinketsStacks)
    }

    private fun getTrinketsStacks(entity: LivingEntity): List<Pair<Item, ISource>> {
        val trinkets = TrinketsApi.getTrinketComponent(entity)
        return trinkets.map {
            it.allEquipped.map { tuple ->
                val stack = tuple.b
                stack.item to TrinketsSource(tuple.a.index, stack)
            }
        }.orElseGet(::emptyList)
    }

}