package com.stevan.colorpicker

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.stevan.colorpickerview.ColorObserver
import com.stevan.colorpickerview.ui.ColorWheelView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultView = findViewById<View>(R.id.resultingColorView)

        val colorWheelView = findViewById<ColorWheelView>(R.id.colorWheelView)
        colorWheelView.setInitialColor(ContextCompat.getColor(this, R.color.colorAccent))
        colorWheelView.subscribe(object : ColorObserver {
            override fun onColor(color: Int, fromUser: Boolean) {
                resultView.setBackgroundColor(color)
            }
        })
    }

}