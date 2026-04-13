package com.example.myapplication.model

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.example.myapplication.enum.*

data class DrawingUiState(
    val drawingTool: DrawingTool = DrawingTool.NONE,
    val color: Int = Color.BLACK,
    val brushSize: Float = 8f,
    val eraseSize: Float = 30f
)
