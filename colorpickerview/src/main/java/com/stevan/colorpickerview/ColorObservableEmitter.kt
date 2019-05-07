package com.stevan.colorpickerview

import android.graphics.Color

class ColorObservableEmitter : ColorObservable {

    private val observers = mutableListOf<ColorObserver>()
    private var color: Int = Color.WHITE

    override fun subscribe(observer: ColorObserver) {
        observers.add(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        observers.remove(observer)
    }

    override fun getColor(): Int = color

    fun onColor(color: Int, fromUser: Boolean) {
        this.color = color
        observers.forEach {
            it.onColor(color, fromUser)
        }
    }

}