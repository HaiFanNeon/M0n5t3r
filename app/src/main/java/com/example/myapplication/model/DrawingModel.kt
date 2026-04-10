package com.example.myapplication.model

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.myapplication.`interface`.Coordinates
import com.example.myapplication.enum.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

data class DrawingUiState(
    val reviewMode: ReviewMode = ReviewMode.NONE,
    val drawingTool: DrawingTool = DrawingTool.NONE,
    val color: Int = Color.BLACK,
    val brushSize: Float = 8f,
    val eraseSize: Float = 30f
)

class DrawingModel {

    /**
     * color：线的颜色
     * stroke：线的宽度
     * drawPath：保存这条线
     */
    data class DrawingPath(
        val drawPath: Path = Path(),
        var color: Int = 0,
        var stroke: Float = 8f
    )

    /**
     * curPath：当前path
     * undoStk：回退栈
     * redoStk：撤销回退栈
     * drawingTool：画笔还是橡皮擦
     */
    data class PathData(
        var curPath: Path = Path(),
        val undoStk: ArrayDeque<DrawingPath> = ArrayDeque(),
        val redoStk: ArrayDeque<DrawingPath> = ArrayDeque(),
        var drawingTool: DrawingTool = DrawingTool.NONE
    )
}

