package com.possible_triangle.flightlib.init

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.platform.Services

object CommonClass {

    val SOUND_WHOOSH = Services.REGISTRIES.registerSound("whoosh")

    fun init() {
        IFlightApi.register(FlightApiImpl)

        CommonSources.register()
    }

    fun clientInit() {

    }

}