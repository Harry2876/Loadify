package com.example.customloaders

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat

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
                loaderColor = getColor(R.styleable.CircularLoaderView_loaderColor,loaderColor)
                loaderSpeed = getInt(R.styleable.CircularLoaderView_loaderSpeed, loaderSpeed)
                strokeWidth = getDimension(R.styleable.CircularLoaderView_strokeWidth, strokeWidth)
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
        val radius = (width / 2f) - (paint.strokeWidth / 2)
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
    fun setLoaderColor(color: Int) {
        loaderColor = color
        invalidate()
    }

    fun getLoaderColor(): Int = loaderColor

    fun setLoaderSize(size: Float) {
        loaderSize = size
        requestLayout() // Adjust layout to accommodate size change
    }

    fun getLoaderSize(): Float = loaderSize

    fun setLoaderSpeed(speed: Int) {
        loaderSpeed = speed
        startRotationAnimation() // Restart animation with new speed
    }

    fun getLoaderSpeed(): Int = loaderSpeed

    fun setStrokeWidth(width: Float) {
        strokeWidth = width
        paint.strokeWidth = width
        invalidate()
    }

    fun getStrokeWidth(): Float = strokeWidth

}