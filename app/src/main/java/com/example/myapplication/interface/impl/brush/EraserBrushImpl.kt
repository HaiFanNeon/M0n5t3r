package com.example.myapplication.`interface`.impl.brush

import android.graphics.Color
import com.example.myapplication.`interface`.BaseBrush

class EraserBrushImpl : BaseBrush() {

    init {
        paint.color = Color.WHITE
    }

    override fun updateConfig(color: Int, size: Float) {
        paint.strokeWidth = size
        paint.color = Color.WHITE
    }
}
