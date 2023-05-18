package com.possible_triangle.flightlib.forge.services

import com.possible_triangle.flightlib.Constants
import com.possible_triangle.flightlib.logic.network.KeyEvent
import com.possible_triangle.flightlib.platform.services.INetwork
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent.Context
import net.minecraftforge.network.NetworkRegistry
import java.util.function.BiConsumer
import java.util.function.Supplier

class ForgeNetwork : INetwork {

    companion object {

        private const val VERSION = "1.0"
        private val CHANNEL = NetworkRegistry.ChannelBuilder.named(ResourceLocation(Constants.MOD_ID, "network"))
            .networkProtocolVersion { VERSION }.clientAcceptedVersions(VERSION::equals)
            .serverAcceptedVersions(VERSION::equals).simpleChannel()

        private fun <T> createHandler(handler: (T, ServerPlayer) -> Unit) =
            BiConsumer<T, Supplier<Context>> { event, supplier ->
                val context = supplier.get()

                val player = context.sender
                if (context.direction == NetworkDirection.PLAY_TO_SERVER && player != null) {
                    context.enqueueWork {
                        handler(event, player)
                    }
                }

                context.packetHandled = true
            }

        fun init() {
            CHANNEL.registerMessage(
                0,
                KeyEvent::class.java,
                KeyEvent::encode,
                KeyEvent::decode,
                createHandler(KeyEvent::handle)
            )
        }
    }

    override fun sendToServer(message: Any) {
        CHANNEL.sendToServer(message)
    }
    
}