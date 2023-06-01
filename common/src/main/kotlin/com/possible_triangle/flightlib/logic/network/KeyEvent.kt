package com.possible_triangle.flightlib.logic.network

import com.possible_triangle.flightlib.Constants.MOD_ID
import com.possible_triangle.flightlib.api.FlightKey
import com.possible_triangle.flightlib.logic.ControlManager
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.ChatType
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerPlayer

class KeyEvent(val key: FlightKey, val pressed: Boolean, val notify: Boolean = false) {

    companion object {

        fun handle(event: KeyEvent, player: ServerPlayer) {
            if (event.notify) player.sendMessage(
                TranslatableComponent(
                    "message.$MOD_ID.control.${event.key.name.lowercase()}",
                    TranslatableComponent("message.$MOD_ID.control.${if (event.pressed) "on" else "off"}")
                ),
                ChatType.GAME_INFO, player.uuid,
            )
            ControlManager.handle(player, event)
        }

        fun encode(event: KeyEvent, packet: FriendlyByteBuf) {
            packet.writeInt(event.key.ordinal)
            packet.writeBoolean(event.pressed)
            packet.writeBoolean(event.notify)
        }

        fun decode(packet: FriendlyByteBuf): KeyEvent {
            val key = FlightKey.values()[packet.readInt()]
            return KeyEvent(key, packet.readBoolean(), packet.readBoolean())
        }

    }

}