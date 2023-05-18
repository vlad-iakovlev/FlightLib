package com.possible_triangle.flightlib.forge.services

import com.possible_triangle.flightlib.platform.services.IPlatformHelper
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.loading.FMLLoader

class ForgePlatformHelper : IPlatformHelper {

    override val platformName = "Forge"

    override fun isModLoaded(modId: String) = ModList.get().isLoaded(modId)

    override val isDevelopmentEnvironment get() = !FMLLoader.isProduction()

}