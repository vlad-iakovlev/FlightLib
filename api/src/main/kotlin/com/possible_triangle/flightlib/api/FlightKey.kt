package com.possible_triangle.flightlib.api

import net.minecraft.client.KeyMapping
import net.minecraft.world.entity.LivingEntity
import org.lwjgl.glfw.GLFW
import java.util.*

enum class FlightKey(val toggle: Boolean, val defaultKey: Int? = null, val default: Boolean = false) {
    UP(false),
    LEFT(false),
    RIGHT(false),
    FORWARD(false),
    BACKWARD(false),
    TOGGLE_ACTIVE(true, default = true, defaultKey = GLFW.GLFW_KEY_G),
    TOGGLE_HOVER(true, defaultKey = GLFW.GLFW_KEY_H);

    lateinit var binding: Optional<KeyMapping>

    fun isPressed(entity: LivingEntity) = IFlightApi.INSTANCE.isPressed(this, entity)

}