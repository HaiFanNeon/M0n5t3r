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


class DrawingModel {

    data class RealCoordinates(
        override var panX: Float = 0f,
        override var panY: Float = 0f,

        override var lastMidX: Float = 0f,
        override var lastMidY: Float = 0f,

        override var lastX: Float = 0f,
        override var lastY: Float = 0f,

        override var scale: Float = 1f,
        override var lastDistance: Float = 0f,

        override var rotation: Float = 0f,
        override var lastRotation: Float = 0f,

        override var initDistance: Float = 0f,
        override var initRotation: Float = 0f,
        override var initMidX: Float = 0f,
        override var initMidY: Float = 0f
    ) : Coordinates {


        fun calcX(x: Float): Float {
            return (x - panX - lastMidX) / scale + lastMidX
        }
        fun calcY(y: Float): Float {
            return (y - panY - lastMidY) / scale + lastMidY
        }

        fun toCanvasX(x: Float) {
            this.lastX = calcX(x)
        }
        fun toCanvasY(y: Float) {
            this.lastY = calcY(y)
        }
    }

    data class ReviewCoordinates (
        override var panX: Float = 0f,
        override var panY: Float = 0f,

        override var lastMidX: Float = 0f,
        override var lastMidY: Float = 0f,

        override var lastX: Float = 0f,
        override var lastY: Float = 0f,

        override var scale: Float = 1f,
        override var lastDistance: Float = 0f,

        override var rotation: Float = 0f,
        override var lastRotation: Float = 0f,

        override var initDistance: Float = 0f,
        override var initRotation: Float = 0f,
        override var initMidX: Float = 0f,
        override var initMidY: Float = 0f
    ) : Coordinates{
    }

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

