package com.example.customloaders.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.customloaders.R

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
        style = Paint.Style.FILL
    }

    // Animation progress for each dot
    private val dotProgress = FloatArray(numDots) { 0f }
    private val dotAnimators = mutableListOf<ValueAnimator>()

    init {
        // Read custom attributes from XML
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TailChaseSpinner, 0, 0)
            loaderSize = typedArray.getDimension(R.styleable.TailChaseSpinner_spinner_size, loaderSize)
            dotColor = typedArray.getColor(R.styleable.TailChaseSpinner_spinner_color, dotColor)
            val spinnerDotSize = typedArray.getFloat(R.styleable.TailChaseSpinner_spinner_dot_size, 0.15f)
            dotSize = loaderSize * spinnerDotSize
            typedArray.recycle()
        }

        paint.color = dotColor

        setupAnimators()
    }

    private fun setupAnimators() {
        dotAnimators.forEach { it.cancel() }
        dotAnimators.clear()

        for (i in 0 until numDots) {
            val animator = ValueAnimator.ofFloat(0f, 360f).apply {
                duration = 1600L // Slightly faster for each dot
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

        val centerX = width / 2f
        val centerY = height / 2f
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

    // Inline setter methods for dynamic updates

    fun setSpinnerSize(size: Float) {
        loaderSize = size
        dotSize = loaderSize * 0.15f // Update dot size proportionally
        invalidate() // Redraw with updated size
    }

    fun setSpinnerColor(color: Int) {
        dotColor = color
        paint.color = dotColor
        invalidate() // Redraw with updated color
    }

    fun setDotSize(percentage: Float) {
        dotSize = loaderSize * percentage
        invalidate() // Redraw with updated dot size
    }
}
