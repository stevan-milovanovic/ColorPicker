package com.stevan.colorpickerview

import android.view.MotionEvent

class ThrottledTouchEventHandler(
    private val updatable: Updatable,
    private val minInterval: Int = Constants.EVENT_MIN_INTERVAL
) {

    private var lastPassedEventTime = 0L

    fun onTouchEvent(event: MotionEvent) {
        val current = System.currentTimeMillis()
        if (current - lastPassedEventTime <= minInterval) return

        lastPassedEventTime = current
        updatable.update(event)
    }

}