package com.jdots.paint.tools.options

import android.graphics.Paint

interface SprayToolOptionsView {

    fun setCallback(callback: Callback?)

    fun setRadius(radius: Int)

    fun setCurrentPaint(paint: Paint)

    interface Callback {

        fun radiusChanged(radius: Int)
    }
}