package com.example.myapplication.ext

import com.example.myapplication.`interface`.Coordinates


fun Coordinates.copyFrom(other: Coordinates) {
    panX = other.panX
    panY = other.panY

    lastMidX = other.lastMidX
    lastMidY = other.lastMidY

    lastX = other.lastX
    lastY = other.lastY

    scale = other.scale
    lastDistance = other.lastDistance

    rotation = other.rotation
    lastRotation = other.lastRotation

    initDistance = other.initDistance
    initRotation = other.initRotation
    initMidX = other.initMidX
    initMidY = other.initMidY
}

fun Coordinates.clear() {
    panX = 0f
    panY = 0f

    lastMidX = 0f
    lastMidY = 0f

    lastX = 0f
    lastY = 0f

    scale = 1f
    lastDistance = 0f

    rotation = 0f
    lastRotation = 0f

    initDistance = 0f
    initRotation = 0f
    initMidX = 0f
    initMidY = 0f
}