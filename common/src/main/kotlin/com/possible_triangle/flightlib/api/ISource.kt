package com.possible_triangle.flightlib.api

import net.minecraft.world.entity.LivingEntity

interface ISource {

    data class ProviderEntry(val source: ISource, val provider: () -> IJetpack?)

    fun interface Provider {
        fun get(entity: LivingEntity): List<ProviderEntry>
    }
}