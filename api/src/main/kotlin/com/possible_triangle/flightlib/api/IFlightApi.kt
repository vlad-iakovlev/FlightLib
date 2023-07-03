package com.possible_triangle.flightlib.api

import net.minecraft.world.entity.LivingEntity

interface IFlightApi {

    companion object {
        lateinit var INSTANCE: IFlightApi
            private set

        fun register(instance: IFlightApi) {
            INSTANCE = instance
        }
    }

    fun addSourceProvider(provider: ISource.Provider)

    fun addSourceCaster(caster: ISource.Caster)

    fun getAll(entity: LivingEntity): List<ISource.ProviderEntry>

    fun findJetpack(entity: LivingEntity): IJetpack.Context?

    fun isActive(type: ControlType, key: FlightKey, entity: LivingEntity): Boolean

    fun isPressed(key: FlightKey, entity: LivingEntity): Boolean

    fun findActiveJetpack(entity: LivingEntity): IJetpack.Context?

}