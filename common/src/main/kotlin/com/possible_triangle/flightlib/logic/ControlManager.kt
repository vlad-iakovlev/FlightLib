package com.possible_triangle.flightlib.logic

import com.mojang.blaze3d.platform.InputConstants
import com.possible_triangle.flightlib.api.FlightKey
import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.logic.network.KeyEvent
import com.possible_triangle.flightlib.platform.Services
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
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

    internal fun sync(event: KeyEvent) {
        val player = Minecraft.getInstance().player ?: return
        Services.NETWORK.sendToServer(event)
        handle(player, event)
    }

    fun onTick(player: LocalPlayer) {
        FlightKey.values().filter { !it.toggle && it.binding.isPresent }.forEach {
            sync(KeyEvent(it, it.binding.get().isDown))
        }

        sync(KeyEvent(FlightKey.UP, player.input.jumping))
        sync(KeyEvent(FlightKey.LEFT, player.input.left))
        sync(KeyEvent(FlightKey.RIGHT, player.input.right))
        sync(KeyEvent(FlightKey.FORWARD, player.input.forwardImpulse > 0))
        sync(KeyEvent(FlightKey.BACKWARD, player.input.forwardImpulse < 0))

    }

    fun checkKeys() {
        val player = Minecraft.getInstance().player ?: return
        IFlightApi.INSTANCE.findJetpack(player) ?: return

        val key = FlightKey.values()
            .filter { it.toggle }
            .firstOrNull { it.binding.get().isDown } ?: return

        sync(KeyEvent(key, !key.isPressed(player), true))

    }

}