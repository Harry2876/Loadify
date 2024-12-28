package com.example.customloaders

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class Particle(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val particles = mutableListOf<ParticleData>()
    private val particleCount = 20
    private val particleRadius = 10f
    private val movementRange = 50f // Limited range to maintain consistent spacing
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var animator: ValueAnimator

    init {
        startAnimation()
    }

    private fun generateParticles() {
        val width = measuredWidth.coerceAtLeast(1)
        val height = measuredHeight.coerceAtLeast(1)

        for (i in 0 until particleCount) {
            val x = Random.nextFloat() * (width - particleRadius * 2) + particleRadius
            val y = Random.nextFloat() * (height - particleRadius * 2) + particleRadius
            val isBright = Random.nextBoolean()
            val color = if (isBright) Color.argb(255, 100, 149, 237) else Color.argb(128, 100, 149, 237)

            // Initialize particle with random position, direction, and speed
            val angle = Random.nextFloat() * 360 // Random direction
            val speed = if (isBright) 1.5f else 1f // Brighter dots move faster
            particles.add(ParticleData(x, y, x, y, angle, speed, color))
        }
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2600 // Update every 16ms (~60 FPS)
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                updateParticles()
                invalidate() // Redraw the particles
            }
            start()
        }
    }

    private fun updateParticles() {
        for (particle in particles) {
            // Calculate new position based on direction and speed
            val dx = cos(Math.toRadians(particle.angle.toDouble())).toFloat() * particle.speed
            val dy = sin(Math.toRadians(particle.angle.toDouble())).toFloat() * particle.speed

            particle.x += dx
            particle.y += dy

            // Prevent particles from going out of bounds
            if (particle.x - particleRadius < 0 || particle.x + particleRadius > measuredWidth) {
                particle.angle = Random.nextFloat() * 360 // Randomize direction upon collision with width boundary
                particle.x = particle.x.coerceIn(particleRadius, measuredWidth - particleRadius) // Clamp position
            }
            if (particle.y - particleRadius < 0 || particle.y + particleRadius > measuredHeight) {
                particle.angle = Random.nextFloat() * 360 // Randomize direction upon collision with height boundary
                particle.y = particle.y.coerceIn(particleRadius, measuredHeight - particleRadius) // Clamp position
            }

            // Prevent excessive spacing during animation
            val distanceFromStart = sqrt((particle.x - particle.startX).pow(2) + (particle.y - particle.startY).pow(2))
            if (distanceFromStart > movementRange) {
                particle.angle = Random.nextFloat() * 360 // Change direction to stay close to start
                particle.x = (particle.startX + dx).coerceIn(particleRadius, measuredWidth - particleRadius)
                particle.y = (particle.startY + dy).coerceIn(particleRadius, measuredHeight - particleRadius)
            }

            // Ensure angle stays in valid range (0 to 360 degrees)
            particle.angle = (particle.angle + 360) % 360
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        particles.clear()
        generateParticles()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (particle in particles) {
            paint.color = particle.color
            canvas.drawCircle(particle.x, particle.y, particleRadius, paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel() // Stop the animator when the view is detached
    }

    private data class ParticleData(
        var x: Float, // Current x position
        var y: Float, // Current y position
        val startX: Float, // Initial x position
        val startY: Float, // Initial y position
        var angle: Float, // Direction of movement
        val speed: Float, // Speed of movement
        val color: Int // Color of the particle
    )
}
