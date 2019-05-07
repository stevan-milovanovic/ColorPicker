package com.stevan.colorpickerview

interface ColorObserver {

    fun onColor(color: Int, fromUser: Boolean)

}