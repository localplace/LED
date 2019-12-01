package com.md.gamepractical.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView

class DragShadowBuilder(v: TextView) : View.DragShadowBuilder(v) {

    private val shadowBorder: TextView = v;

    private val shadow = ColorDrawable(Color.YELLOW)

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = view.width

        val height: Int = view.height
        shadow.setBounds(4, 4, width - 4, height - 4)
        size.set(width, height)
        touch.set(width, height )
    }

    override fun onDrawShadow(canvas: Canvas) {
        shadow.draw(canvas)
        shadowBorder.draw(canvas)
    }
}
