package com.possible_triangle.flightlib.forge.compat

import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.ISource
import com.possible_triangle.flightlib.api.sources.CuriosSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent
import top.theillusivec4.curios.api.CuriosCapability
import top.theillusivec4.curios.api.SlotTypeMessage
import top.theillusivec4.curios.api.SlotTypePreset

object CuriosCompat {

    fun register(modBus: IEventBus) {
        if (!ModList.get().isLoaded("curios")) return

        modBus.addListener { _: InterModEnqueueEvent ->
            InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE) {
                SlotTypePreset.BACK.messageBuilder.build()
            }
        }

        IFlightApi.INSTANCE.addSourceProvider(::getCuriosStacks)
    }

    private fun getCuriosStacks(entity: LivingEntity): List<Pair<Item,ISource>> {
        val curios = entity.getCapability(CuriosCapability.INVENTORY)
        return curios.map {
            it.curios.entries.flatMap { (slot, handler) ->
                val slots = 0 until handler.slots
                slots.map { index ->
                    val stack = handler.stacks.getStackInSlot(index)
                    stack.item to (CuriosSource(slot, index, stack))
                }
            }
        }.orElseGet(::emptyList)
    }

}