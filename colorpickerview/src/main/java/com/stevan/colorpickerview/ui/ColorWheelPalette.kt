package com.stevan.colorpickerview.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

internal class ColorWheelPalette @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private var huePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var saturationPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val newWidth = w - paddingStart - paddingEnd
        val newHeight = h - paddingTop - paddingBottom

        radius = Math.min(newWidth, newHeight) * 0.5f

        if (radius < 0) return

        centerX = w * 0.5f
        centerY = h * 0.5f

        val hueShader = SweepGradient(
            centerX,
            centerY,
            intArrayOf(Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED),
            null
        )
        huePaint.shader = hueShader

        val saturationShader =
            RadialGradient(centerX, centerY, radius, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP)
        saturationPaint.shader = saturationShader
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(centerY, centerY, radius, huePaint)
        canvas?.drawCircle(centerY, centerY, radius, saturationPaint)
    }

}