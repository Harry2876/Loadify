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

package com.example.customloaders.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.customloaders.R

class CircularLoaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var rotationDegree = 0f
    private var loaderColor = Color.BLUE
    private var loaderSize = 100f
    private var loaderSpeed = 1200
    private var strokeWidth = 15f

    // Declare animator as a class-level variable so it can be referenced in other methods
    private var animator: ValueAnimator? = null

    private var sweepGradient: SweepGradient? = null // Preallocated gradient

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE // Use stroke style to create the circular loader
    }

    init {
        //Loading Custom Settings
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircularLoaderView,
            0,0
        ).apply {
            try {
                loaderColor = getColor(R.styleable.CircularLoaderView_circle_color,loaderColor)
                loaderSpeed = getInt(R.styleable.CircularLoaderView_circle_speed, loaderSpeed)
                strokeWidth = getDimension(R.styleable.CircularLoaderView_circle_stroke, strokeWidth)
                loaderSize = getDimension(R.styleable.CircularLoaderView_circle_size, loaderSize)
            } finally {
                recycle()
            }
        }
        paint.strokeWidth = strokeWidth
        paint.color = loaderColor
        // Set up a linear animation to spin the loader
        startRotationAnimation()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateSweepGradient()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = sweepGradient

        // Rotate the canvas to create a spinning effect
        canvas.save()
        canvas.rotate(rotationDegree, width / 2f, height / 2f)

        // Draw a circular arc around the center of the view
        val radius = (loaderSize / 2f) - (paint.strokeWidth / 2)
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)

        canvas.restore()
    }

    private fun updateSweepGradient() {
        // Set up the conic-like gradient
        sweepGradient = SweepGradient(
            width / 2f, height / 2f,
            intArrayOf(Color.TRANSPARENT, loaderColor, loaderColor, Color.TRANSPARENT),
            floatArrayOf(0.04f, 0.75f, 0.25f, 0f)
        )
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            startRotationAnimation()
        } else {
            animator?.cancel() // stop animation if not visible
        }
    }

    private fun startRotationAnimation() {
        // Initialize animator only if it hasn't been initialized already
        if (animator == null) {
            animator = ValueAnimator.ofFloat(0f, 360f).apply {
                duration = loaderSpeed.toLong()
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener {
                    rotationDegree = it.animatedValue as Float
                    invalidate() // Redraw the view with the updated rotation
                }
                start()
            }
        } else if (!animator!!.isRunning) {
            animator!!.start()
        }
    }

    //Adding getters and setters for custom properties
    fun setCircleColor(color: Int) {
        loaderColor = color
        invalidate()
    }

    fun getCircleColor(): Int = loaderColor

    fun setCircleSize(size: Float) {
        loaderSize = size
        requestLayout() // Adjust layout to accommodate size change
        invalidate()
    }

    fun getCircleSize(): Float = loaderSize

    fun setCircleSpeed(speed: Int) {
        loaderSpeed = speed
        startRotationAnimation() // Restart animation with new speed
    }

    fun getCircleSpeed(): Int = loaderSpeed

    fun setCircleStroke(width: Float) {
        strokeWidth = width
        paint.strokeWidth = width
        invalidate()
    }

    fun setCircleStroke(): Float = strokeWidth

}