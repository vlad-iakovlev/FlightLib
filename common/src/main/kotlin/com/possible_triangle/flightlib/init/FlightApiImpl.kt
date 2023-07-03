package com.possible_triangle.flightlib.init

import com.possible_triangle.flightlib.api.*
import com.possible_triangle.flightlib.logic.ControlManager
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

object FlightApiImpl : IFlightApi {
    private val PROVIDERS = arrayListOf<ISource.Provider>()
    private val CASTERS = arrayListOf<ISource.Caster>()

    override fun addSourceProvider(provider: ISource.Provider) {
        PROVIDERS.add(provider)
    }

    override fun addSourceCaster(caster: ISource.Caster) {
        CASTERS.add(caster)
    }

    override fun getAll(entity: LivingEntity): List<ISource.ProviderEntry> {
        val objects = PROVIDERS.flatMap { it.get(entity) }
        return objects.flatMap { (value, source) ->
            CASTERS.flatMap { caster ->
                caster.get(value).map {
                    ISource.ProviderEntry(source, it)
                }
            }
        }
    }

    override fun findJetpack(entity: LivingEntity): IJetpack.Context? {
        val world = entity.level ?: return null
        val pose = FlyingPose.get(entity)
        return getAll(entity).asSequence().map { it.source to it.provider() }
            .filter { (_, jetpack) -> jetpack != null }
            .map { (source, jetpack) -> IJetpack.Context.builder(entity, world, pose, source) to jetpack }
            .map { (builder, jetpack) -> builder to jetpack!! }
            .map { (builder, jetpack) -> builder(jetpack) }.filter { it.jetpack.isValid(it) }.firstOrNull()
    }

    override fun isActive(type: ControlType, key: FlightKey, entity: LivingEntity): Boolean {
        return when (type) {
            ControlType.ALWAYS -> true
            ControlType.NEVER -> false
            ControlType.TOGGLE -> key.isPressed(entity)
        }
    }

    override fun findActiveJetpack(entity: LivingEntity): IJetpack.Context? {
        if (entity is Player && entity.abilities.flying) return null
        return findJetpack(entity)?.takeIf {
            isActive(
                it.jetpack.activeType(it),
                FlightKey.TOGGLE_ACTIVE,
                entity
            )
        }?.takeIf { it.jetpack.isUsable(it) }
    }

    override fun isPressed(key: FlightKey, entity: LivingEntity) = ControlManager.isPressed(key, entity)
}