package com.possible_triangle.flightlib.forge.services

import com.possible_triangle.flightlib.Constants.MOD_ID
import com.possible_triangle.flightlib.platform.services.IRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

class ForgeRegistries : IRegistries {

    companion object {
        private val SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID)

        fun register(bus: IEventBus) {
            SOUNDS.register(bus)
        }
    }

    override val swimSpeed get() = ForgeMod.SWIM_SPEED.get()

    override fun registerSound(name: String): () -> SoundEvent {
        val registered = SOUNDS.register(name) { SoundEvent.createVariableRangeEvent(ResourceLocation(MOD_ID, name)) }
        return registered::get
    }
}