package com.possible_triangle.flightlib.init

import com.possible_triangle.flightlib.api.ControlType
import com.possible_triangle.flightlib.api.IJetpack
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.phys.Vec3

object DevJetpack : IJetpack {

    private val THRUSTERS = listOf(Vec3(0.0, 0.8, -0.25))

    override fun horizontalSpeed(context: IJetpack.Context) = 0.02

    override fun verticalSpeed(context: IJetpack.Context) = 0.4

    override fun acceleration(context: IJetpack.Context) = 0.6

    override fun hoverType(context: IJetpack.Context) = ControlType.TOGGLE

    override fun hoverSpeed(context: IJetpack.Context) = 0.0

    override fun swimModifier(context: IJetpack.Context) = 1.8

    override fun isValid(context: IJetpack.Context) = true

    override fun isUsable(context: IJetpack.Context) = true

    override fun getThrusters(context: IJetpack.Context) = THRUSTERS

    override fun createParticles(): SimpleParticleType = ParticleTypes.FLAME

}