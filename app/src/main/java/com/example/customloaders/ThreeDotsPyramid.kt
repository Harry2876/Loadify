package com.example.customloaders

import android.animation.ValueAnimator
import android.content.Context
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

    private var loaderSize: Float = 200f // Default pyramid size
    private var dotColor: Int = Color.WHITE // Default dot color
    private var dotSize: Float = loaderSize * 0.15f // Size of each dot
    private var rotationAngle = 0f // Current rotation angle
    private var triangleRadius: Float = loaderSize / 3.5f // Initial radius of the triangle

    private var rotationAnimator: ValueAnimator? = null
    private var radiusAnimator: ValueAnimator? = null

    init {
        // Load custom attributes from XML
        context.theme.obtainStyledAttributes(attrs, R.styleable.ThreeDotsPyramid, 0, 0).apply {
            try {
                loaderSize = getDimension(R.styleable.ThreeDotsPyramid_pyramid_size, loaderSize)
                dotColor = getColor(R.styleable.ThreeDotsPyramid_pyramid_color, dotColor)
            } finally {
                recycle()
            }
        }

        // Update derived values
        dotSize = loaderSize * 0.15f
        triangleRadius = loaderSize / 3.5f

        setupAnimators()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = dotColor
    }

    private fun setupAnimators() {
        // Cancel previous animators (if any)
        rotationAnimator?.cancel()
        radiusAnimator?.cancel()

        // Animator for rotating the triangle
        rotationAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 1100L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                rotationAngle = animation.animatedValue as Float
                invalidate()
            }
        }

        // Animator for expanding and contracting the triangle radius
        radiusAnimator = ValueAnimator.ofFloat(triangleRadius * 0.4f, triangleRadius).apply {
            duration = 800L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                triangleRadius = animation.animatedValue as Float
                invalidate()
            }
        }

        // Start animations
        rotationAnimator?.start()
        radiusAnimator?.start()
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
        rotationAnimator?.cancel()
        radiusAnimator?.cancel()
    }

    // Inline setters for runtime updates

    fun setPyramidSize(size: Float) {
        loaderSize = size
        dotSize = loaderSize * 0.15f // Adjust dot size proportionally
        triangleRadius = loaderSize / 3.5f // Adjust triangle radius
        invalidate() // Redraw with updated size
        setupAnimators() // Restart animators to reflect size change
    }

    fun setDotColor(color: Int) {
        dotColor = color
        paint.color = dotColor
        invalidate() // Redraw with updated color
    }

    fun setDotSize(percentage: Float) {
        dotSize = loaderSize * percentage
        invalidate() // Redraw with updated dot size
    }
}
