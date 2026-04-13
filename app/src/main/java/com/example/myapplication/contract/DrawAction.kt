package com.example.myapplication.contract

import android.graphics.Canvas

interface DrawAction {
    fun draw(canvas: Canvas)
    fun offset(dx: Float, dy: Float)
}