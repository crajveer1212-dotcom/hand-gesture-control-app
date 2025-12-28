package com.handgesture.control

import kotlin.math.abs
import kotlin.math.sqrt

class GestureDetector {
    
    private var lastPosition: Pair<Float, Float>? = null
    private var lastMoveTime = 0L
    private var stablePositionTime = 0L
    private var lastZoomDistance: Float? = null
    
    companion object {
        private const val SWIPE_THRESHOLD = 0.15f // 15% of screen
        private const val STABLE_TIME_FOR_TAP_MS = 1000L
        private const val MOVE_THRESHOLD = 0.02f // 2% movement = not stable
        private const val ZOOM_THRESHOLD = 0.05f // 5% change to trigger zoom
    }
    
    data class GestureAction(
        val type: ActionType,
        val position: Pair<Float, Float>? = null,
        val direction: SwipeDirection? = null,
        val zoomFactor: Float? = null
    )
    
    enum class ActionType {
        MOVE_CURSOR,
        TAP,
        SWIPE,
        ZOOM_IN,
        ZOOM_OUT,
        NONE
    }
    
    enum class SwipeDirection {
        LEFT, RIGHT, UP, DOWN
    }
    
    fun detectAction(
        currentPosition: Pair<Float, Float>,
        gestureType: GestureType,
        zoomDistance: Float? = null
    ): GestureAction {
        
        val currentTime = System.currentTimeMillis()
        
        return when (gestureType) {
            GestureType.POINT -> detectPointingAction(currentPosition, currentTime)
            GestureType.ZOOM -> detectZoomAction(zoomDistance, currentTime)
            else -> GestureAction(ActionType.NONE)
        }
    }
    
    private fun detectPointingAction(
        position: Pair<Float, Float>,
        currentTime: Long
    ): GestureAction {
        
        val lastPos = lastPosition
        
        if (lastPos == null) {
            lastPosition = position
            stablePositionTime = currentTime
            return GestureAction(ActionType.MOVE_CURSOR, position)
        }
        
        val distance = calculateDistance(lastPos, position)
        
        // Check if position is stable (for tap)
        if (distance < MOVE_THRESHOLD) {
            val stableTime = currentTime - stablePositionTime
            if (stableTime >= STABLE_TIME_FOR_TAP_MS) {
                stablePositionTime = currentTime
                return GestureAction(ActionType.TAP, position)
            }
            return GestureAction(ActionType.MOVE_CURSOR, position)
        }
        
        stablePositionTime = currentTime
        
        // Check for swipe gesture
        if (distance > SWIPE_THRESHOLD) {
            val direction = detectSwipeDirection(lastPos, position)
            lastPosition = position
            return GestureAction(ActionType.SWIPE, position, direction)
        }
        
        lastPosition = position
        return GestureAction(ActionType.MOVE_CURSOR, position)
    }
    
    private fun detectZoomAction(
        zoomDistance: Float?,
        currentTime: Long
    ): GestureAction {
        
        if (zoomDistance == null) {
            return GestureAction(ActionType.NONE)
        }
        
        val lastDist = lastZoomDistance
        
        if (lastDist == null) {
            lastZoomDistance = zoomDistance
            return GestureAction(ActionType.NONE)
        }
        
        val change = zoomDistance - lastDist
        val changePercent = abs(change / lastDist)
        
        if (changePercent < ZOOM_THRESHOLD) {
            return GestureAction(ActionType.NONE)
        }
        
        lastZoomDistance = zoomDistance
        
        return if (change > 0) {
            GestureAction(ActionType.ZOOM_IN, zoomFactor = 1.0f + changePercent)
        } else {
            GestureAction(ActionType.ZOOM_OUT, zoomFactor = 1.0f - changePercent)
        }
    }
    
    private fun detectSwipeDirection(
        from: Pair<Float, Float>,
        to: Pair<Float, Float>
    ): SwipeDirection {
        val dx = to.first - from.first
        val dy = to.second - from.second
        
        return if (abs(dx) > abs(dy)) {
            if (dx > 0) SwipeDirection.RIGHT else SwipeDirection.LEFT
        } else {
            if (dy > 0) SwipeDirection.DOWN else SwipeDirection.UP
        }
    }
    
    private fun calculateDistance(
        point1: Pair<Float, Float>,
        point2: Pair<Float, Float>
    ): Float {
        val dx = point2.first - point1.first
        val dy = point2.second - point1.second
        return sqrt(dx * dx + dy * dy)
    }
    
    fun reset() {
        lastPosition = null
        lastMoveTime = 0L
        stablePositionTime = 0L
        lastZoomDistance = null
    }
}
