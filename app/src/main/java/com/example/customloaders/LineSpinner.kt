package com.example.customloaders

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator

class LineSpinnerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var loaderSize = 110f // Hardcoded loader size
    private var loaderColor = Color.WHITE // Hardcoded loader color
    private var loaderStroke = 10f // Hardcoded loader stroke width
    private var bars = 35 // Hardcoded number of bars
    private var paint: Paint = Paint()
    private var currentBar = 1

    init {
        paint.color = loaderColor
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        // Set a repeating runnable to update the current bar
        postDelayed(object : Runnable {
            override fun run() {
                currentBar = (currentBar + 4) % bars
                postInvalidateOnAnimation() // Redraw the view
                postDelayed(this, 45) // Set delay for the next update
            }
         }, 100)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = loaderSize / 2f

        for (i in 0 until bars) {
            val angle = i * 360 / bars
            val rect = RectF(
                radius - loaderStroke / 2,
                0f,
                radius + loaderStroke / 2,
                loaderSize * 0.22f
            )

            // Create a rounded rectangle for the bar
            val roundedRect = RectF(
                rect.left,
                rect.top,
                rect.right,
                rect.bottom
            )
            val cornerRadius = loaderStroke / 2 // Use half the stroke for rounded corners




            // Set alpha based on the current animated bar
            paint.alpha = when {
                i == currentBar -> 275// Fully visible for the current bar
                i == (currentBar + 4) % bars -> (255 * 0.5).toInt() // Slightly faded for the next bar
                else -> 0 // Fully transparent for all other bars
            }


            canvas.save()
            canvas.rotate(angle.toFloat(), radius, radius)


            canvas.drawRoundRect(roundedRect, cornerRadius, cornerRadius, paint)
            canvas.restore()
        }
    }
}