package com.possible_triangle.flightlib.forge.api

import com.possible_triangle.flightlib.api.IJetpack
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken

object ForgeFlightLib {

    val JETPACK_CAPABILITY: Capability<IJetpack> = CapabilityManager.get(object : CapabilityToken<IJetpack>() {})

}