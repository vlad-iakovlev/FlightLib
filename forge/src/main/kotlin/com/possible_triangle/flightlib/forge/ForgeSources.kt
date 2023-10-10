package com.possible_triangle.flightlib.forge

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.IJetpack
import com.possible_triangle.flightlib.forge.api.ForgeFlightLib
import net.minecraftforge.common.capabilities.ICapabilityProvider

object ForgeSources {

    private fun ICapabilityProvider.asJetpack(): IJetpack? {
        val capability = getCapability(ForgeFlightLib.JETPACK_CAPABILITY)
        return if (capability.isPresent) capability.resolve().get()
        else null
    }

    fun register() {
        IFlightApi.INSTANCE.addSourceCaster {
            listOf {
                if(it is ICapabilityProvider) it.asJetpack()
                else null
            }
        }
    }

}