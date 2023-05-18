package com.possible_triangle.flightlib.logic

import com.mojang.blaze3d.platform.InputConstants
import com.possible_triangle.flightlib.api.FlightKey
import com.possible_triangle.flightlib.logic.network.KeyEvent
import net.minecraft.client.KeyMapping
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import java.util.*
import java.util.function.Consumer

object ControlManager {

    private val KEYS = mutableMapOf<Player, MutableMap<FlightKey, Boolean>>()

    internal fun isPressed(key: FlightKey, entity: LivingEntity): Boolean {
        if (entity !is Player) return false
        return KEYS[entity]?.get(key) ?: key.default
    }

    private fun setKey(player: Player, key: FlightKey, pressed: Boolean) {
        val keys = KEYS.getOrPut(player) { mutableMapOf() }
        keys[key] = pressed
    }

    fun registerKeybinds(registry: Consumer<KeyMapping>) {
        FlightKey.values().forEach { key ->
            key.binding = Optional.ofNullable(key.defaultKey).map {
                KeyMapping(
                    "key.jetpack.${key.name.lowercase()}.description",
                    InputConstants.Type.KEYSYM,
                    it,
                    "key.categories.movement.jetpack"
                )
            }
            key.binding.ifPresent {
                registry.accept(it)
            }
        }
    }

    internal fun handle(player: Player, event: KeyEvent) {
        setKey(player, event.key, event.pressed)
    }

    fun reset(player: Player) {
        KEYS[player]?.apply {
            FlightKey.values()
                .filterNot { it.toggle }
                .forEach { remove(it) }
        }
    }

}