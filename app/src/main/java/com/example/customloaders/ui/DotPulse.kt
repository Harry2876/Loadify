package com.example.customloaders.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.customloaders.R

class DotPulse @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Default Properties
    private var dpColor = Color.WHITE // Dot color
    private var dpSize = 40f // Size of each dot
    private var dpSpacing = 20f // Spacing between dots
    private var dpDotsNum = 3 // Number of dots
    private var dpSpeed = 400L // Duration of the pulse animation in milliseconds

    private lateinit var dotScales: FloatArray // Scale for each dot
    private val dotAnimators = mutableListOf<ValueAnimator>() // List of animators for each dot

    init {
        // Read custom attributes from XML
        context.theme.obtainStyledAttributes(attrs, R.styleable.DotPulse, 0, 0).apply {
            try {
                dpColor = getColor(R.styleable.DotPulse_dot_color, dpColor)
                dpSize = getDimension(R.styleable.DotPulse_dot_size, dpSize)
                dpSpacing = getDimension(R.styleable.DotPulse_dot_spacing, dpSpacing)
                dpDotsNum = getInt(R.styleable.DotPulse_dot_count, dpDotsNum)
                dpSpeed = getInteger(R.styleable.DotPulse_dot_speed, dpSpeed.toInt()).toLong()
            } finally {
                recycle()
            }
        }

        // Initialize dot scales
        dotScales = FloatArray(dpDotsNum) { 0f }
        startAnimation()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dpColor
        style = Paint.Style.FILL
    }


    // Start the animation for all dots
    private fun startAnimation() {
        // Clear previous animators if any
        dotAnimators.forEach { it.cancel() }
        dotAnimators.clear()

        for (i in 0 until dpDotsNum) {
            val animator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = dpSpeed
                startDelay = i * (dpSpeed / dpDotsNum) // Stagger animation start for each dot
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE // Reverse to create a pulse effect
                interpolator = LinearInterpolator() // Linear interpolation for smooth animation
                addUpdateListener { animation ->
                    dotScales[i] = animation.animatedValue as Float
                    invalidate() // Redraw with updated scale values
                }
            }
            dotAnimators.add(animator)
            animator.start() // Start animation for each dot
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calculate the starting X position for the dots (center them)
        val totalWidth = (dpSize * dpDotsNum) + (dpSpacing * (dpDotsNum - 1))
        val startX = (width - totalWidth) / 2f
        val centerY = height / 2f

        for (i in 0 until dpDotsNum) {
            val x = startX + i * (dpSize + dpSpacing)
            val scale = dotScales[i]
            canvas.drawCircle(x, centerY, (dpSize / 2) * scale, paint)
        }
    }

    // Cleanup when view is detached from window
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dotAnimators.forEach { it.cancel() } // Cancel all animators
    }

    // Public methods to allow programmatic updates
    fun setDotColor(color: Int) {
        dpColor = color
        paint.color = color
        invalidate()
    }

    fun setDotSize(size: Float) {
        dpSize = size
        invalidate()
    }

    fun setDotSpacing(spacing: Float) {
        dpSpacing = spacing
        invalidate()
    }

    fun setDotCount(count: Int) {
        dpDotsNum = count
        dotScales = FloatArray(dpDotsNum) { 0f }
        startAnimation()
    }

    fun setDotSpeed(speed: Long) {
        dpSpeed = speed
        startAnimation()
    }
}
