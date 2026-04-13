package com.example.myapplication.factory

import android.graphics.Color
import com.example.myapplication.enum.DrawingTool
import com.example.myapplication.`interface`.BaseBrush
import com.example.myapplication.`interface`.impl.brush.EraserBrushImpl
import com.example.myapplication.`interface`.impl.brush.PenBrushImpl

object BrushFactory {

    private val brushMap = mutableMapOf<DrawingTool, (Int, Float, Float) -> BaseBrush>()

    init {
        register(DrawingTool.BRUSH) { color, stroke, _ ->
            PenBrushImpl().apply {
                updateConfig(color, stroke)
            }
        }
        register(DrawingTool.ERASE) { _, _, eraseSize ->
            EraserBrushImpl().apply {
                updateConfig(Color.WHITE, eraseSize)
            }
        }
    }

    fun register(tool: DrawingTool, creator: (Int, Float, Float) -> BaseBrush) {
        brushMap[tool] = creator
    }

    fun create(tool: DrawingTool, color: Int, stroke: Float, eraseSize: Float): BaseBrush? {
        return brushMap[tool]?.invoke(color, stroke, eraseSize)
    }

}