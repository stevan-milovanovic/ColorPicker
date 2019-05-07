package com.stevan.colorpickerview

interface ColorObservable {

    fun subscribe(observer: ColorObserver)

    fun unsubscribe(observer: ColorObserver)

    fun getColor(): Int

}