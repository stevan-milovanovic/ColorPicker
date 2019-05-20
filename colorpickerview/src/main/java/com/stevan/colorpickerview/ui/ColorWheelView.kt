package com.stevan.colorpickerview.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.stevan.colorpickerview.*

class ColorWheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ColorObservable,
    Updatable {

    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private lateinit var selector: ColorWheelSelector
    private lateinit var initialSelector: ColorWheelSelector

    private val emitter = ColorObservableEmitter()
    private val handler = ThrottledTouchEventHandler(this)

    private var selectorRadiusPx: Float = 0f

    private var currentPoint: PointF = PointF()
    private var currentColor = Color.WHITE

    private var initialColor: Int? = null

    init {
        selectorRadiusPx = Constants.SELECTOR_RADIUS_DP * resources.displayMetrics.density
        addColorWheelPaletteView()
        addInitialSelectorView()
        addSelectorView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        val size = Math.min(maxWidth, maxHeight)
        val measuredSize = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(measuredSize, measuredSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val newWidth = w - paddingStart - paddingEnd
        val newHeight = h - paddingTop - paddingBottom
        radius = Math.min(newWidth, newHeight) * 0.5f - selectorRadiusPx

        if (radius < 0) return

        centerX = newWidth * 0.5f
        centerY = newHeight * 0.5f
        setColor(currentColor)
        setInitialColorSelectorPoint()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                handler.onTouchEvent(event)
                return true
            }
            MotionEvent.ACTION_UP -> {
                update(event)
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    override fun update(event: MotionEvent) {
        val x = event.x
        val y = event.y
        emitter.onColor(getColorAtPoint(x, y), true)
        updateSelector(x, y)
    }

    override fun subscribe(observer: ColorObserver) {
        emitter.subscribe(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        emitter.unsubscribe(observer)
    }

    override fun getColor(): Int = emitter.getColor()

    fun hideInitialColor() {
        removeView(initialSelector)
    }

    fun setInitialColor(color: Int) {
        initialColor = color
        addInitialSelectorView()
        setInitialColorSelectorPoint()
    }

    fun setCurrentColor(color: Int) {
        currentColor = color
        setColor(currentColor)
    }

    private fun getColorAtPoint(eventX: Float, eventY: Float): Int {
        val x = (eventX - centerX).toDouble()
        val y = (eventY - centerY).toDouble()
        val r = Math.sqrt(x * x + y * y)
        val hsv = floatArrayOf(0f, 0f, 1f)
        hsv[0] = (Math.atan2(y, -x) / Math.PI * 180f).toFloat() + 180
        hsv[1] = Math.max(0f, Math.min(1f, (r / radius).toFloat()))
        return Color.HSVToColor(hsv)
    }

    private fun setColor(color: Int) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        val r = hsv[1] * radius
        val radian = hsv[0] / 180f * Math.PI
        updateSelector(
            (r * Math.cos(radian) + centerX).toFloat(),
            (-r * Math.sin(radian) + centerY).toFloat()
        )
        currentColor = color
        emitter.onColor(color, false)
    }

    private fun setInitialColorSelectorPoint() {
        if (initialColor == null) return

        val hsv = FloatArray(3)
        Color.colorToHSV(initialColor!!, hsv)
        val saturationRadius = hsv[1] * radius
        val radian = hsv[0] / 180f * Math.PI

        val initialX = (saturationRadius * Math.cos(radian) + centerX).toFloat()
        val initialY = (-saturationRadius * Math.sin(radian) + centerY).toFloat()

        var x = (initialX - centerX).toDouble()
        var y = (initialY - centerY).toDouble()
        val r = Math.sqrt(x * x + y * y)

        if (r > radius) {
            x *= radius / r
            y *= radius / r
        }

        val initialPoint = PointF(x.toFloat() + centerX, y.toFloat() + centerY)
        initialSelector.point = initialPoint
    }

    private fun updateSelector(eventX: Float, eventY: Float) {
        var x = (eventX - centerX).toDouble()
        var y = (eventY - centerY).toDouble()
        val r = Math.sqrt(x * x + y * y)

        if (r > radius) {
            x *= radius / r
            y *= radius / r
        }

        currentPoint.x = x.toFloat() + centerX
        currentPoint.y = y.toFloat() + centerY
        selector.point = currentPoint
    }

    private fun addSelectorView() {
        val size = (Constants.SELECTOR_RADIUS_DP * 2 * resources.displayMetrics.density).toInt()
        val layoutParams = LayoutParams(size, size)
        selector = ColorWheelSelector(context)
        selector.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.selector))
        addView(selector, layoutParams)
    }

    private fun addInitialSelectorView() {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        initialSelector = ColorWheelSelector(context)
        initialSelector.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.selector
            )
        )
        initialSelector.alpha = 0.75f
        addView(initialSelector, layoutParams)
    }

    private fun addColorWheelPaletteView() {
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val palette = ColorWheelPalette(context)
        val padding = selectorRadiusPx.toInt()
        palette.setPadding(padding, padding, padding, padding)
        addView(palette, layoutParams)
    }

}