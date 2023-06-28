package com.possible_triangle.flightlib.forge

import com.possible_triangle.flightlib.api.IJetpack
import com.possible_triangle.flightlib.forge.api.ForgeFlightLib
import com.possible_triangle.flightlib.init.CommonSources
import net.minecraftforge.common.capabilities.ICapabilityProvider

object ForgeSources {

    fun ICapabilityProvider.asJetpack(): IJetpack? {
        val capability = getCapability(ForgeFlightLib.JETPACK_CAPABILITY)
        return if (capability.isPresent) capability.resolve().get()
        else null
    }

    fun register() {
        CommonSources.addEntitySources { (it as ICapabilityProvider).asJetpack() }
        CommonSources.addEquipmentSources { (it as ICapabilityProvider).asJetpack() }
    }

}