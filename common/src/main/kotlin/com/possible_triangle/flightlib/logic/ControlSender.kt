package com.possible_triangle.flightlib.logic

import com.possible_triangle.flightlib.api.FlightKey
import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.logic.network.KeyEvent
import com.possible_triangle.flightlib.platform.Services
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer

object ControlSender {

    private var LAST_PRESS = mutableMapOf<FlightKey, Long>()

    private fun FlightKey.canPressAgain(): Boolean {
        return LAST_PRESS[this]?.let {
            (System.currentTimeMillis() - it) > 150
        } ?: true
    }

    private fun sync(event: KeyEvent) {
        val player = Minecraft.getInstance().player ?: return
        Services.NETWORK.sendToServer(event)
        ControlManager.handle(player, event)
    }

    fun checkKeys() {
        val player = Minecraft.getInstance().player ?: return
        IFlightApi.INSTANCE.findJetpack(player) ?: return

        FlightKey.values()
            .filter { it.toggle }
            .filter { it.binding.get().isDown }
            .filter { it.canPressAgain() }
            .forEach { key ->
                LAST_PRESS[key] = System.currentTimeMillis()
                sync(KeyEvent(key, !key.isPressed(player), true))
            }
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

}