package com.example.customloaders.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customloaders.R
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class Particle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Customizable properties with default values
    private var pcBrightColor = 0xFF6495ED.toInt() // Default bright color
    private var pcLightColor = 0x806495ED.toInt() // Default light color
    private var pcCount = 20 // Default particle count
    private var pcRadius = 10f // Default particle radius
    private var pcMovementRange = 60f // Default movement range
    private val particles = mutableListOf<ParticleData>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var animator: ValueAnimator

    init {
        // Read custom attributes
//        context.theme.obtainStyledAttributes(attrs, R.styleable.Particle, 0, 0).apply {
//            try {
//                pcBrightColor = getColor(R.styleable.Particle_particle_bright_color, pcBrightColor)
//                pcLightColor = getColor(R.styleable.Particle_particle_light_color, pcLightColor)
//                pcCount = getInt(R.styleable.Particle_particle_count, pcCount)
//                pcRadius = getDimension(R.styleable.Particle_particle_radius, pcRadius)
//            } finally {
//                recycle()
//            }
//        }
        startAnimation()
    }

    private fun generateParticles() {
        val width = measuredWidth.coerceAtLeast(1)
        val height = measuredHeight.coerceAtLeast(1)

        for (i in 0 until pcCount) {
            val x = Random.nextFloat() * (width - pcRadius * 2) + pcRadius
            val y = Random.nextFloat() * (height - pcRadius * 2) + pcRadius
            val isBright = Random.nextBoolean()
            val color = if (isBright) pcBrightColor else pcLightColor

            // Initialize particle with random position, direction, and speed
            val angle = Random.nextFloat() * 360 // Random direction
            val speed = if (isBright) 1.5f else 1f // Brighter dots move faster
            particles.add(ParticleData(x, y, x, y, angle, speed, color))
        }
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1200 // Animation duration
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                updateParticles()
                invalidate() // Redraw the particles
            }
            start()
        }
    }

    private fun updateParticles() {
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()

        for (particle in particles) {
            // Calculate new position based on direction and speed
            val dx = cos(Math.toRadians(particle.angle.toDouble())).toFloat() * particle.speed
            val dy = sin(Math.toRadians(particle.angle.toDouble())).toFloat() * particle.speed

            particle.x += dx
            particle.y += dy

            // Ensure the bounds for coerceIn are valid
            val minX = pcRadius
            val maxX = (width - pcRadius).coerceAtLeast(minX) // Prevent maxX < minX
            val minY = pcRadius
            val maxY = (height - pcRadius).coerceAtLeast(minY) // Prevent maxY < minY

            // Constrain particle within bounds
            particle.x = particle.x.coerceIn(minX, maxX)
            particle.y = particle.y.coerceIn(minY, maxY)

            // Ensure particles stay close to their starting position
            val distanceFromStart = sqrt((particle.x - particle.startX).pow(2) + (particle.y - particle.startY).pow(2))
            if (distanceFromStart > pcMovementRange ) {
                particle.angle = Random.nextFloat() * 760 // Randomize direction
            }
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
            canvas.drawCircle(particle.x, particle.y, pcRadius, paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel() // Stop the animator when the view is detached
    }
//
//    fun setBrightColor(color: Int) {
//        pcBrightColor = color
//        regenerateParticles()
//    }
//
//    fun setLightColor(color: Int) {
//        pcLightColor = color
//        regenerateParticles()
//    }
//
//    fun setParticleCount(count: Int) {
//        pcCount = count
//        regenerateParticles()
//    }
//
//    fun setParticleRadius(radius: Float) {
//        pcRadius = radius
//        regenerateParticles()
//    }


    // Helper methods to regenerate particles and restart animation
    private fun regenerateParticles() {
        particles.clear()
        generateParticles()
        invalidate()
    }

    private fun restartAnimation() {
        animator.cancel()
        startAnimation()
    }

    // Data class to represent a particle
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
