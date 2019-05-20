package com.stevan.colorpicker

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.stevan.colorpickerview.ColorObserver
import com.stevan.colorpickerview.ui.ColorWheelView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colorWheelView = findViewById<ColorWheelView>(R.id.colorWheelView)
        colorWheelView.setCurrentColor(ContextCompat.getColor(this, R.color.colorAccent))
        colorWheelView.hideInitialColor()

        colorWheelView.subscribe(object : ColorObserver {
            override fun onColor(color: Int, fromUser: Boolean) {
                window.statusBarColor = color
                supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
            }
        })

        findViewById<Button>(R.id.setInitialColorButton).setOnClickListener {
            colorWheelView.setInitialColor(ContextCompat.getColor(this, R.color.colorAccent))
        }

        findViewById<Button>(R.id.setCurrentColorButton).setOnClickListener {
            colorWheelView.setCurrentColor(Color.YELLOW)
        }
    }

}