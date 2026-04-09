package com.example.myapplication.view

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.net.Uri
import android.os.Build

import android.util.AttributeSet
import android.util.Log

import android.view.MotionEvent
import android.view.View

import androidx.annotation.RequiresApi

import androidx.core.graphics.createBitmap
import com.example.myapplication.enum.*
import com.example.myapplication.ext.clear
import com.example.myapplication.ext.copyFrom
import com.example.myapplication.`interface`.Coordinates
import com.example.myapplication.`interface`.DrawingInterface
import com.example.myapplication.model.DrawingModel.*



class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), DrawingInterface {

    val pathData = PathData()
    var reviewMode = ReviewMode.NONE
    var realCoordinates: RealCoordinates = RealCoordinates()
    var reviewCoordinates: ReviewCoordinates = ReviewCoordinates()
    var touchType: TouchType = TouchType.NONE
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas

    var onSaveBitmapListener : (() -> Unit)? = null

    private var drawPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private var earsePaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 30f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.i("testtest", w.toString() + "===" + h.toString())
        if (w > 0 && h > 0 && !::bitmap.isInitialized) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            bitmapCanvas = Canvas(bitmap)
            bitmapCanvas.drawColor(Color.WHITE)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cdt = getCoordinates()
        canvas.save()
        canvas.translate(cdt.panX, cdt.panY)
        canvas.scale(cdt.scale, cdt.scale, cdt.lastMidX, cdt.lastMidY)
        canvas.rotate(cdt.rotation, cdt.lastMidX, cdt.lastMidY)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.restore()
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        if (!::bitmap.isInitialized) return false
        val x: Float = event.x
        val y: Float = event.y

        val isReviewMode = getModel() == ReviewMode.REVIEW

        if (isReviewMode) {
            return reviewTouchEvent(event, x, y)
        } else {
            return editTouchEvent(event, x, y)
        }
    }

    override fun getDistance(event: MotionEvent) : Float {
        if (event.pointerCount < 2) return 0f
        val dx = event.getX(0) - event.getX(1)
        val dy = event.getY(0) - event.getY(1)

        return kotlin.math.sqrt(dx * dx + dy * dy)
    }

    override fun getMidPoint(event: MotionEvent) : Pair<Float, Float> {
        val dx = (event.getX(0) + event.getX(1)) / 2f
        val dy = (event.getY(0) + event.getY(1)) / 2f

        return Pair(dx, dy)
    }

    override fun getAngle(event: MotionEvent): Float {
        val dx = event.getX(1) - event.getX(0)
        val dy = event.getY(1) - event.getY(0)
        return Math.toDegrees(kotlin.math.atan2(dy, dx).toDouble()).toFloat()
    }
    override fun setEraseMode() {
        setEdit()
        pathData.drawingTool = DrawingTool.ERASE
    }

    override fun setBrushMode() {
        setEdit()
        pathData.drawingTool = DrawingTool.BRUSH
    }

    override fun setBitmap(bitmap: Bitmap) {
        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        this.bitmap = newBitmap
        bitmapCanvas = Canvas(this.bitmap)
        invalidate()
    }

    override fun getBitmap(): Bitmap {
        return bitmap
    }
    override fun clear() {
        if (width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmapCanvas = Canvas(bitmap)
        }
        bitmapCanvas.drawColor(Color.WHITE)
        setEraseMode()
        realCoordinates.clear()
        reviewCoordinates.clear()
        pathData.undoStk.clear()
        pathData.redoStk.clear()
        onSaveBitmapListener?.invoke()
        invalidate()
    }
    override fun setReview() {
        reviewMode = ReviewMode.REVIEW
    }
    override fun setEdit() {
        reviewMode = ReviewMode.NONE
        realCoordinates.copyFrom(reviewCoordinates)
        invalidate()
    }
    override fun getModel(): ReviewMode {
        return reviewMode
    }

    override fun undo() : Boolean {
        return true
    }

    override fun redo(): Boolean {
        return true
    }

    override fun gettRealCoordinates(): RealCoordinates {
        return realCoordinates
    }

    override fun gettReviewCoordinates(): ReviewCoordinates {
        return reviewCoordinates
    }

    override fun settRealCoordinates(other: Coordinates) {
        realCoordinates.copyFrom(other)
    }

    override fun settReviewCoordinates(other: Coordinates) {
        reviewCoordinates.copyFrom(other)
    }

    private fun drawPathAll() {
        return
    }

    private fun getPaint() : Paint {
        return if (pathData.drawingTool == DrawingTool.BRUSH) drawPaint else earsePaint
    }

    private fun getCoordinates(): Coordinates {
        return if (getModel() == ReviewMode.REVIEW) reviewCoordinates else realCoordinates
    }

    private fun extBitmap(x: Float, y:Float) {
        var limit = 100f

        if (x in 0f..(bitmap.width.toFloat() - limit) && y in 0f..(bitmap.height.toFloat() - limit)) return

        val newWeight = Math.max(bitmap.width + 1000, (x + 500).toInt())
        val newHeight = Math.max(bitmap.height + 1000, (y + 500).toInt())

        val offsetX = (newWeight - bitmap.width) / 2f
        val offsetY = (newHeight - bitmap.height) / 2f

        val newBitmap = createBitmap(newWeight, newHeight)
        val newCanvas = Canvas(newBitmap).apply {
            drawColor(Color.WHITE)
            drawBitmap(bitmap, offsetX, offsetY, null)
        }

        realCoordinates.panX -= offsetX
        realCoordinates.panY -= offsetY
        realCoordinates.lastX += offsetX
        realCoordinates.lastY += offsetY
        reviewCoordinates.copyFrom(realCoordinates)

        pathData.curPath.offset(offsetX, offsetY)
        bitmap = newBitmap
        bitmapCanvas = newCanvas
    }

    private fun reviewTouchEvent(event: MotionEvent, x: Float, y: Float): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchType = TouchType.Gesture(GestureType.MOVE)
                reviewCoordinates.lastX = x
                reviewCoordinates.lastY = y
                return true
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount >= 2) {
                    touchType = TouchType.Gesture(GestureType.NONE)
                    val (midX, midY) = getMidPoint(event)
                    reviewCoordinates.initMidX = midX
                    reviewCoordinates.initMidY = midY
                    reviewCoordinates.initDistance = getDistance(event)
                    reviewCoordinates.initRotation = getAngle(event)

                    reviewCoordinates.lastMidX = midX
                    reviewCoordinates.lastMidY = midY
                    reviewCoordinates.lastDistance = reviewCoordinates.initDistance
                    reviewCoordinates.lastRotation = reviewCoordinates.initRotation
                }
            }

            MotionEvent.ACTION_MOVE -> {
                when(val t = touchType) {
                    is TouchType.Gesture -> {
                        if (event.pointerCount < 2) {
                            reviewCoordinates.panX += x - reviewCoordinates.lastX
                            reviewCoordinates.panY += y - reviewCoordinates.lastY
                            reviewCoordinates.lastX = x
                            reviewCoordinates.lastY = y

                        } else {
                            val newDistance = getDistance(event)
                            val newRotation = getAngle(event)
                            val (midX, midY) = getMidPoint(event)

                            var currentGesture = t.type

                            if (currentGesture == GestureType.NONE) {
                                val distanceDiff = kotlin.math.abs(newDistance - reviewCoordinates.initDistance)
                                var angleDiff = kotlin.math.abs(newRotation - reviewCoordinates.initRotation)

                                if (angleDiff > 180) angleDiff = 360 - angleDiff

                                currentGesture = when {
                                    distanceDiff > 40f-> GestureType.SCALE
                                    angleDiff > 8f -> GestureType.ROTATE
                                    else -> GestureType.NONE
                                }

                                if (currentGesture != GestureType.NONE) {
                                    touchType = TouchType.Gesture(currentGesture)
                                    reviewCoordinates.lastDistance = newDistance
                                    reviewCoordinates.lastRotation = newRotation
                                    reviewCoordinates.lastMidX = midX
                                    reviewCoordinates.lastMidY = midY
                                }
                            }
                            when (currentGesture) {
                                GestureType.SCALE -> {
                                    if (reviewCoordinates.lastDistance > 0) {
                                        val scaleFactor = newDistance / reviewCoordinates.lastDistance
                                        reviewCoordinates.scale *= scaleFactor
                                    }
                                }
                                GestureType.ROTATE -> {
                                    var angleDiff = newRotation - reviewCoordinates.lastRotation
                                    if (angleDiff > 180) angleDiff -= 360
                                    if (angleDiff < -180) angleDiff += 360
                                    reviewCoordinates.rotation += angleDiff
                                }
                                else -> {}
                            }
                            reviewCoordinates.lastDistance = newDistance
                            reviewCoordinates.lastRotation = newRotation
                            reviewCoordinates.lastMidX = midX
                            reviewCoordinates.lastMidY = midY
                        }
                        invalidate()
                    }
                    else -> {}
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                touchType = TouchType.NONE
                onSaveBitmapListener?.invoke()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun editTouchEvent(event: MotionEvent, x: Float, y: Float): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchType = TouchType.PEN
                val canvasX = realCoordinates.calcX(x)
                val canvasY = realCoordinates.calcY(y)
                extBitmap(canvasX, canvasY)
                realCoordinates.toCanvasY(y)
                realCoordinates.toCanvasX(x)
                pathData.curPath.reset()
                pathData.curPath.moveTo(realCoordinates.lastX, realCoordinates.lastY)
                return true
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount >= 2) {
                    touchType = TouchType.NONE
                }
            }

            MotionEvent.ACTION_MOVE -> {
                when(val t = touchType) {
                    is TouchType.PEN -> {
                        var canvasX = realCoordinates.calcX(x)
                        var canvasY = realCoordinates.calcY(y)
                        extBitmap(canvasX, canvasY)
                        canvasX = realCoordinates.calcX(x)
                        canvasY = realCoordinates.calcY(y)
                        pathData.curPath.quadTo(realCoordinates.lastX, realCoordinates.lastY,
                            (realCoordinates.lastX + canvasX) / 2f, (realCoordinates.lastY + canvasY) / 2f)
                        realCoordinates.toCanvasX(x)
                        realCoordinates.toCanvasY(y)
                        val paint = if (pathData.drawingTool == DrawingTool.ERASE) earsePaint else drawPaint
                        bitmapCanvas.drawPath(pathData.curPath, paint)
                        invalidate()
                    }
                    else -> {}
                }
            }

            MotionEvent.ACTION_UP -> {
                val paint = if (pathData.drawingTool == DrawingTool.ERASE) earsePaint else drawPaint
                bitmapCanvas.drawPath(pathData.curPath, paint)
                pathData.curPath.reset()
                touchType = TouchType.NONE
                onSaveBitmapListener?.invoke()
            }

            MotionEvent.ACTION_POINTER_UP -> {
                if (event.pointerCount <= 2) {
                    touchType = TouchType.NONE
                }
            }
        }
        return super.onTouchEvent(event)
    }
}