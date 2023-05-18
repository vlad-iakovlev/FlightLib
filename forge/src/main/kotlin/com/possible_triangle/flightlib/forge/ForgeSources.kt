package com.possible_triangle.flightlib.forge

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.ISource
import com.possible_triangle.flightlib.api.sources.EntitySource
import com.possible_triangle.flightlib.api.sources.EquipmentSource
import com.possible_triangle.flightlib.forge.api.ForgeFlightLib
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.capabilities.ICapabilityProvider

object ForgeSources {

    fun ItemStack.asProvider(source: ISource) = (this as ICapabilityProvider).asProvider(source)

    fun ICapabilityProvider.asProvider(source: ISource): ISource.ProviderEntry {
        return ISource.ProviderEntry(source) {
            val capability = getCapability(ForgeFlightLib.JETPACK_CAPABILITY)
            if (capability.isPresent) capability.resolve().get()
            else null
        }
    }

    fun register() {
        IFlightApi.INSTANCE.addSource { listOf((it as ICapabilityProvider).asProvider(EntitySource)) }
        IFlightApi.INSTANCE.addSource { entity ->
            EquipmentSlot.values().map {
                val stack = entity.getItemBySlot(it)
                stack.asProvider(EquipmentSource(it))
            }
        }
    }

}