package com.example.myapplication.enum

enum class DrawingTool {
    NONE,
    BRUSH,
    ERASE
}

enum class GestureType {
    NONE,
    MOVE,
    SCALE,
    ROTATE
}


sealed class TouchType {
    object NONE: TouchType()
    object PEN: TouchType()
    data class Gesture(val type: GestureType) : TouchType()
}



enum class ReviewMode {
    NONE,
    REVIEW
}
