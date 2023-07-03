package com.possible_triangle.flightlib.fabric

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.IJetpack

object FabricSources {

    fun register() {
        IFlightApi.INSTANCE.addSourceCaster {
            listOf {
                if(it is IJetpack) it
                else null
            }
        }
    }

}