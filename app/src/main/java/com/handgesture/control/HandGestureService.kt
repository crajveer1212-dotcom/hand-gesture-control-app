package com.handgesture.control

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class HandGestureService : AccessibilityService() {
    
    companion object {
        private var instance: HandGestureService? = null
        
        fun getInstance(): HandGestureService? = instance
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not needed for our use case
    }
    
    override fun onInterrupt() {
        // Handle interruption
    }
    
    fun performTap(x: Float, y: Float) {
        val path = Path()
        path.moveTo(x, y)
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()
        
        dispatchGesture(gesture, null, null)
    }
    
    fun performSwipe(fromX: Float, fromY: Float, toX: Float, toY: Float, duration: Long = 300) {
        val path = Path()
        path.moveTo(fromX, fromY)
        path.lineTo(toX, toY)
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()
        
        dispatchGesture(gesture, null, null)
    }
    
    fun performPinchZoom(centerX: Float, centerY: Float, zoomIn: Boolean) {
        val distance = if (zoomIn) 200f else -200f
        
        val path1 = Path()
        path1.moveTo(centerX - 50, centerY)
        path1.lineTo(centerX - 50 - distance, centerY)
        
        val path2 = Path()
        path2.moveTo(centerX + 50, centerY)
        path2.lineTo(centerX + 50 + distance, centerY)
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path1, 0, 300))
            .addStroke(GestureDescription.StrokeDescription(path2, 0, 300))
            .build()
        
        dispatchGesture(gesture, null, null)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}
