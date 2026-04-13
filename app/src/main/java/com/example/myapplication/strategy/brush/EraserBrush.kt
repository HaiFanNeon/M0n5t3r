package com.example.myapplication.strategy.brush

import android.graphics.Color

class EraserBrush : BaseBrush() {

    init {
        paint.color = Color.WHITE
    }

    override fun updateConfig(color: Int, size: Float) {
        paint.strokeWidth = size
        paint.color = Color.WHITE
    }
}
