package com.stevan.colorpickerview.ui

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

internal class ColorWheelSelector @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var point: PointF = PointF()
        set(value) {
            field = value
            x = value.x - measuredWidth / 2f
            y = value.y - measuredHeight / 2f
            invalidate()
        }

}