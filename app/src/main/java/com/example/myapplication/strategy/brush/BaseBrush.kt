package com.example.myapplication.strategy.brush

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

abstract class BaseBrush {
    val path = Path()
    val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    open fun updateConfig(color: Int, size: Float) {
        paint.color = color
        paint.strokeWidth = size
    }

    open fun onTouchDown(x: Float, y: Float) {
        path.reset()
        path.moveTo(x, y)
    }

    open fun onTouchMove(x: Float, y: Float, lastX: Float, lastY: Float) {
        path.quadTo(x, y, (lastX + x) / 2f, (lastY + y) / 2f)
    }

    open fun onTouchUp(bitmapCanvas: Canvas) {
        bitmapCanvas.drawPath(path, paint)
        path.reset()
    }

    open fun drawPreview(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }
}
