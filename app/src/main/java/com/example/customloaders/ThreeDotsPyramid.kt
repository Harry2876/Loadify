// ThreeDotsPyramid.kt

package com.example.customloaders

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class ThreeDotsPyramid @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var loaderSize: Float
    private var dotColor: Int
    private var dotSize: Float // Size of each dot
    private var rotationAngle = 0f // Current rotation angle
    private var triangleRadius: Float // Initial radius of the triangle

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        // Load custom attributes
        context.theme.obtainStyledAttributes(attrs, R.styleable.ThreeDotsPyramid, 0, 0).apply {
            try {
                loaderSize = getDimension(R.styleable.ThreeDotsPyramid_tdpLoaderSize, 200f)
                dotColor = getColor(R.styleable.ThreeDotsPyramid_tdpDotColor, Color.BLACK)
            } finally {
                recycle()
            }
        }

        // Initialize paint color
        paint.color = dotColor

        // Set dot size and triangle radius based on loader size
        dotSize = loaderSize * 0.15f
        triangleRadius = loaderSize / 3.5f

        setupAnimators()
    }

    private fun setupAnimators() {
        // Animator for rotating the triangle
        val rotationAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1100L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                rotationAngle = animation.animatedValue as Float
                invalidate()
            }
        }

        // Animator for expanding and contracting the triangle radius
        val radiusAnimator = ValueAnimator.ofFloat(triangleRadius * 0.4f, triangleRadius).apply {
            duration = 800L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                triangleRadius = animation.animatedValue as Float
                invalidate()
            }
        }

        rotationAnimator.start()
        radiusAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = loaderSize / 2f
        val centerY = loaderSize / 2f

        // Rotate the canvas to apply rotation to all dots as a group
        canvas.save()
        canvas.rotate(rotationAngle, centerX, centerY)

        // Draw each dot in a triangular arrangement with animated radius
        for (i in 0 until 3) {
            val angle = Math.toRadians((i * 120).toDouble())
            val x = centerX + (triangleRadius * Math.cos(angle)).toFloat()
            val y = centerY + (triangleRadius * Math.sin(angle)).toFloat()

            // Draw the dot
            canvas.drawCircle(x, y, dotSize / 2, paint)
        }

        canvas.restore() // Restore canvas to avoid affecting other elements
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clean up animations when view is detached
    }
}