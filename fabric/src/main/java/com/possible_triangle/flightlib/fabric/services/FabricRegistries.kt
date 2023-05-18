package com.possible_triangle.flightlib.fabric.services

import com.possible_triangle.flightlib.Constants
import com.possible_triangle.flightlib.platform.services.IRegistries
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

class FabricRegistries : IRegistries {

    override val swimSpeed = null

    override fun registerSound(name: String): () -> SoundEvent {
        val id = ResourceLocation(Constants.MOD_ID, name)
        val registered = Registry.register(Registry.SOUND_EVENT, id, SoundEvent(id))
        return { registered }
    }

}