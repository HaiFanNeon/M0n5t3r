package com.example.myapplication.contract.impl.action

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.example.myapplication.contract.DrawAction


class PathActionImpl(val paint: Paint, val path: Path): DrawAction {
    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    override fun offset(dx: Float, dy: Float) {
        path.offset(dx, dy)
    }
}