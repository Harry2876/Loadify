/**
 * Copyright 2024 Hariom Harsh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hariomharsh.loaders.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.hariomharsh.loaders.R


class LineSpinnerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var loaderSize = 110f
    private var loaderColor = Color.WHITE
    private var loaderStroke = 14f
    private var bars = 15
    private var lineSpeed = 800L
    private val paint: Paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val barPositions = mutableListOf<BarPosition>() // Precompute bar positions
    private var animator: ValueAnimator? = null
    private var animatedProgress = 5f

    init {
        // Setting up XML attributes
        context.theme.obtainStyledAttributes(attrs, R.styleable.LineSpinnerView, 0, 0).apply {
            try {
                loaderSize = getDimension(R.styleable.LineSpinnerView_line_size, loaderSize)
                loaderColor = getColor(R.styleable.LineSpinnerView_line_color, loaderColor)
                loaderStroke = getDimension(R.styleable.LineSpinnerView_line_stroke, loaderStroke)
                lineSpeed = getInt(R.styleable.LineSpinnerView_line_speed, lineSpeed.toInt()).toLong()
            } finally {
                recycle()
            }
        }

        paint.color = loaderColor

        calculateBarPositions() // Calculate positions only once
        setupAnimator()
    }

    private fun calculateBarPositions() {
        val radius = loaderSize / 2f
        barPositions.clear()
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
            duration = lineSpeed
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

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = loaderSize / 2f

        // Translate the canvas to center the loader
        canvas.save()
        canvas.translate(centerX - radius, centerY - radius)

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

    // Inline functions to update properties
    fun setLoaderSize(size: Float) {
        loaderSize = size
        calculateBarPositions()
        invalidate()
    }

    fun setLoaderColor(color: Int) {
        loaderColor = color
        paint.color = color
        invalidate()
    }

    fun setLoaderStroke(stroke: Float) {
        loaderStroke = stroke
        calculateBarPositions()
        invalidate()
    }

    fun setLoaderSpeed(speed : Long) {
        lineSpeed = speed
        setupAnimator()
    }

    // Data class to store bar position information
    private data class BarPosition(val angle: Int, val rect: RectF)
}
