package com.example.myapplication.`interface`

import kotlinx.serialization.Serializable

@Serializable
interface Coordinates {
    var panX: Float
    var panY: Float

    var lastMidX: Float
    var lastMidY: Float

    var lastX: Float
    var lastY: Float

    var scale: Float
    var lastDistance: Float

    var rotation: Float
    var lastRotation: Float

    var initDistance: Float
    var initRotation: Float
    var initMidX: Float
    var initMidY: Float
}
