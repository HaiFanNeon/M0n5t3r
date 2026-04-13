package com.example.myapplication.`interface`

import android.graphics.Canvas

interface DrawAction {
    fun draw(canvas: Canvas)
    fun offset(dx: Float, dy: Float)
}