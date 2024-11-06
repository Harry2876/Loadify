// TailChaseSpinner.kt

package com.example.customloaders

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class TailChaseSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var loaderSize = 200f // Spinner size
    private var dotColor = Color.YELLOW // Dot color
    private var dotSize = loaderSize * 0.15f // Dot size
    private val numDots = 6 // Number of dots in the spinner

    // Paint object for dots
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dotColor
        style = Paint.Style.FILL
    }

    // Animation progress for each dot
    private val dotProgress = FloatArray(numDots) { 0f }
    private val dotAnimators = mutableListOf<ValueAnimator>()

    init {
        setupAnimators()
    }

    private fun setupAnimators() {
        for (i in 0 until numDots) {
            val animator = ValueAnimator.ofFloat(0f, 360f).apply {
                duration = 1600L   // Slightly faster for each dot
                repeatCount = ValueAnimator.INFINITE

                startDelay = i * 150L // Staggered delay for chasing effect
                addUpdateListener { animation ->
                    dotProgress[i] = animation.animatedValue as Float
                    invalidate() // Trigger redraw with updated positions
                }
            }
            dotAnimators.add(animator)
            animator.start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = loaderSize / 2f
        val centerY = loaderSize / 2f
        val radius = loaderSize * 0.4f // Radius of the circular path for dots

        // Apply initial rotation offset so the first dot starts at the top (90°)
        canvas.rotate(270f, centerX, centerY)

        for (i in 0 until numDots) {
            val angle = Math.toRadians(dotProgress[i].toDouble())
            val x = centerX + (radius * Math.cos(angle)).toFloat()
            val y = centerY + (radius * Math.sin(angle)).toFloat()

            // Alpha for the trailing effect
            paint.alpha = ((255 * (1f - i / numDots.toFloat()))).toInt()
            canvas.drawCircle(x, y, dotSize / 2, paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dotAnimators.forEach { it.cancel() }
    }
}