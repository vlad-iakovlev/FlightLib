package com.possible_triangle.flightlib.fabric.services

import com.possible_triangle.flightlib.Constants
import com.possible_triangle.flightlib.logic.network.KeyEvent
import com.possible_triangle.flightlib.platform.services.INetwork
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.resources.ResourceLocation

class FabricNetwork : INetwork {

    companion object {
        private val PACKET_ID = ResourceLocation(Constants.MOD_ID, "key_packet")

        fun register() {
            ServerPlayNetworking.registerGlobalReceiver(PACKET_ID) { server, player, _, buffer, _ ->
                val event = KeyEvent.decode(buffer)
                server.execute {
                    KeyEvent.handle(event, player)
                }
            }
        }

    }

    override fun sendToServer(message: Any) {
        if (message is KeyEvent) {
            val buffer = PacketByteBufs.create()
            KeyEvent.encode(message, buffer)
            ClientPlayNetworking.send(PACKET_ID, buffer)
        }
    }

}