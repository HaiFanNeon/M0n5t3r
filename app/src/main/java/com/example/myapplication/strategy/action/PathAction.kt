package com.example.myapplication.strategy.action

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path


class PathAction(val paint: Paint, val path: Path): DrawAction {
    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun offset(dx: Float, dy: Float) {
        path.offset(dx, dy)
    }
}