package com.possible_triangle.flightlib.logic

import com.possible_triangle.flightlib.Constants
import com.possible_triangle.flightlib.api.FlightKey
import com.possible_triangle.flightlib.api.FlyingPose
import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.IJetpack.Context
import com.possible_triangle.flightlib.init.CommonClass
import com.possible_triangle.flightlib.mixins.ServerGamePacketListenerImplAccessor
import com.possible_triangle.flightlib.platform.Services
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.max
import kotlin.math.min

object JetpackLogic {

    private val DIRECTIONS = listOf(
        FlightKey.BACKWARD to Vec3(0.0, 0.0, -1.0).scale(0.8),
        FlightKey.FORWARD to Vec3(0.0, 0.0, 1.0).scale(1.2),
        FlightKey.LEFT to Vec3(1.0, 0.0, 0.0),
        FlightKey.RIGHT to Vec3(-1.0, 0.0, 0.0),
    )

    private val ATTRIBUTE_ID = UUID.fromString("f4f2d961-fac9-42c2-93b8-69abd884d386")

    private fun handleSwimModifier(entity: LivingEntity, context: Context?) {
        val attribute = Services.REGISTRIES.swimSpeed?.let { entity.getAttribute(it) } ?: return

        val hasModifier = attribute.getModifier(ATTRIBUTE_ID) != null
        val shouldHaveModifier = context?.pose == FlyingPose.SUPERMAN && entity.isUnderWater

        if (!shouldHaveModifier && hasModifier) attribute.removeModifier(ATTRIBUTE_ID)
        else if (shouldHaveModifier && !hasModifier) {
            val modifier = context!!.jetpack.swimModifier(context)
            if (modifier > 0) attribute.addPermanentModifier(
                AttributeModifier(
                    ATTRIBUTE_ID, "${Constants.MOD_ID}:boost", modifier, Operation.MULTIPLY_TOTAL
                )
            )
        }
    }

    fun onTick(entity: LivingEntity) {
        val context = IFlightApi.INSTANCE.findActiveJetpack(entity)
        handleSwimModifier(entity, context)

        if (context == null) return

        val isUsed = when (context.pose) {
            FlyingPose.SUPERMAN -> elytraBoost(context)
            FlyingPose.UPRIGHT -> uprightMovement(context)
        }

        if (isUsed && context.jetpack.isThrusting(context)) {
            spawnParticles(context)
            playSound(context)
            context.jetpack.onUse(context)
        }
    }

    private fun playSound(context: Context) {
        val pos = context.entity.blockPosition()

        val volume = if (FlightKey.UP.isPressed(context.entity)) 2F else 1F
        val pitch = context.world.random.nextFloat() * 0.4F + 1F

        fun SoundEvent.play(volume: Float = 1F, pitch: Float = 1F) {
            context.world.playSound(
                null, pos, this, SoundSource.PLAYERS, volume, pitch
            )
        }

        if (context.entity.isUnderWater) {

            if (context.world.gameTime % 10 != 0L) return

            val (sound, volumeModifier) = when (context.pose) {
                FlyingPose.SUPERMAN -> SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE to -0.5F
                else -> SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT to 0F
            }

            sound.play(volume + volumeModifier, pitch - 0.5F)
        } else {
            if (context.world.gameTime % 5 != 0L) return
            CommonClass.SOUND_WHOOSH().play(volume, pitch)
        }
    }

    private fun elytraBoost(ctx: Context): Boolean {
        if (!ctx.jetpack.boostsElytra()) return false

        val entity = ctx.entity
        if (!entity.isFallFlying) return true
        if (entity !is Player || !FlightKey.UP.isPressed(entity)) return false

        if (entity.level().gameTime % 15 == 0L) {
            val look = entity.lookAngle
            val factor = { i: Double -> i * 0.1 + (i * 1.1 - i) * 0.5 }
            entity.deltaMovement = entity.deltaMovement.add(
                factor(look.x),
                factor(look.y),
                factor(look.z),
            )

        }

        return true
    }

    private fun uprightMovement(ctx: Context): Boolean {
        val entity = ctx.entity
        val buttonUp = FlightKey.UP.isPressed(entity)
        val buttonDown = entity.isShiftKeyDown
        val hovering = IFlightApi.INSTANCE.isActive(ctx.jetpack.hoverType(ctx), FlightKey.TOGGLE_HOVER, entity)

        if (ctx.entity.vehicle != null) return false
        if (ctx.entity.onGround() && !buttonUp) return false

        val verticalSpeed = if (hovering) ctx.jetpack.hoverVerticalSpeed(ctx)
        else ctx.jetpack.verticalSpeed(ctx)

        val horizontalSpeed = if (hovering) {
            if (entity.isUnderWater) 0.0
            else ctx.jetpack.hoverHorizontalSpeed(ctx)
        } else ctx.jetpack.horizontalSpeed(ctx)
        val acceleration = ctx.jetpack.acceleration(ctx)

        val speed = when {
            buttonUp && !buttonDown -> verticalSpeed
            buttonDown && !buttonUp -> -verticalSpeed
            hovering && entity.isUnderWater -> 0.0
            hovering -> ctx.jetpack.hoverSpeed(ctx)
            else -> null
        }

        if (speed != null) {

            if (entity is Player) {
                DIRECTIONS.filter { it.first.isPressed(entity) }.forEach {
                    val vec = Vec3(it.second.x, 0.0, it.second.z).scale(horizontalSpeed)
                    entity.moveRelative(1F, vec)
                }
            }

            val motion = entity.deltaMovement

            val motionY = if (speed <= 0) max(motion.y, speed)
            else min(motion.y + acceleration, speed)

            entity.setDeltaMovement(motion.x, motionY, motion.z)

            if (entity is ServerPlayer) {
                entity.fallDistance = 0F
                (entity.connection as ServerGamePacketListenerImplAccessor).setAboveGroundTickCount(0);
            }
        }

        return true
    }

    private fun spawnParticles(context: Context) {
        val world = context.world

        val thrusters = context.jetpack.getThrusters(context) ?: return
        val yaw = (context.entity.yBodyRot / 180 * -Math.PI).toFloat()
        val pitch = (context.entity.xRot / 180 * -Math.PI).toFloat()
        val xRot = when (context.pose) {
            FlyingPose.SUPERMAN -> pitch
            FlyingPose.UPRIGHT -> 0F
        }
        thrusters.map { it.xRot(xRot) }.map { it.yRot(yaw) }.forEach { pos ->
            val particle = if (context.entity.isUnderWater) ParticleTypes.BUBBLE
            else context.jetpack.createParticles()
            world.addParticle(
                particle,
                context.entity.x + pos.x,
                context.entity.y + pos.y,
                context.entity.z + pos.z,
                0.0,
                -1.0,
                0.0
            )
        }
    }

}