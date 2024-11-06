package com.example.customloaders

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

class LineSpinnerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var loaderSize = 110f
    private var loaderColor = Color.WHITE
    private var loaderStroke = 14f
    private var bars = 15
    private val paint: Paint = Paint().apply {
        color = loaderColor
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val barPositions = mutableListOf<BarPosition>() // Precompute bar positions
    private var animator: ValueAnimator? = null
    private var animatedProgress = 5f

    init {
        calculateBarPositions() // Calculate positions only once
        setupAnimator()
    }

    private fun calculateBarPositions() {
        val radius = loaderSize / 2f
        for (i in 0 until bars) {
            val angle = i * 360 / bars
            val rect = RectF(
                radius - loaderStroke / 2,
                0f,
                radius + loaderStroke / 2,
                loaderSize * 0.22f
            )
            barPositions.add(BarPosition(angle, rect))
        }
    }

    private fun setupAnimator() {
        animator = ValueAnimator.ofFloat(0f, bars.toFloat()).apply {
            duration = 1000L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                animatedProgress = animation.animatedValue as Float
                invalidate() // Trigger redraw with updated alpha
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = loaderSize / 2f

        for ((index, bar) in barPositions.withIndex()) {
            // Calculate alpha based on the distance from the animated progress
            val distanceFromProgress = (animatedProgress - index + bars) % bars + 3.5
            val normalizedDistance = distanceFromProgress / bars.toFloat()

            paint.alpha = (255 * (1 - normalizedDistance)).toInt().coerceIn(0, 255)

            canvas.save()
            canvas.rotate(bar.angle.toFloat(), radius, radius)
            canvas.drawRoundRect(bar.rect, loaderStroke / 2, loaderStroke / 2, paint)
            canvas.restore()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    // Data class to store bar position information
    private data class BarPosition(val angle: Int, val rect: RectF)
}