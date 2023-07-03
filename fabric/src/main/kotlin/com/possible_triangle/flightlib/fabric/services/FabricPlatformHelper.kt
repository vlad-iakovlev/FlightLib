package com.possible_triangle.flightlib.fabric.services

import com.possible_triangle.flightlib.platform.services.IPlatformHelper
import net.fabricmc.loader.api.FabricLoader

class FabricPlatformHelper : IPlatformHelper {

    override val platformName = "Fabric"

    override fun isModLoaded(modId: String) = FabricLoader.getInstance().isModLoaded(modId)

    override val isDevelopmentEnvironment get() = FabricLoader.getInstance().isDevelopmentEnvironment

}