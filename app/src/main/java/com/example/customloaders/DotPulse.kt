package com.example.customloaders

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class DotPulse @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Properties
    private val dotColor = Color.WHITE // Dot color
    private val dotSize = 40f // Size of each dot
    private val dotSpacing = 0f // Spacing between dots
    private val numDots = 3 // Number of dots
    private val animationDuration = 400L // Duration of the pulse animation

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dotColor
        style = Paint.Style.FILL
    }

    private val dotScales = FloatArray(numDots) { 0f } // Scale for each dot
    private val dotAnimators = mutableListOf<ValueAnimator>() // List of animators for each dot

    init {
        startAnimation()
    }

    // Start the animation for all dots
    private fun startAnimation() {
        // Clear previous animators if any
        dotAnimators.forEach { it.cancel() }
        dotAnimators.clear()

        for (i in 0 until numDots) {
            val animator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = animationDuration
                startDelay = i * (animationDuration / numDots) // Stagger animation start for each dot
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
        val totalWidth = (dotSize * numDots) + (dotSpacing * (numDots - 1))
        val startX = (width - totalWidth) / 2f
        val centerY = height / 2f

        for (i in 0 until numDots) {
            val x = startX + i * (dotSize + dotSpacing)
            val scale = dotScales[i]
            canvas.drawCircle(x, centerY, (dotSize / 2) * scale, paint)
        }
    }

    // Cleanup when view is detached from window
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dotAnimators.forEach { it.cancel() } // Cancel all animators
    }
}
