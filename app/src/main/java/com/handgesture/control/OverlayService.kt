package com.handgesture.control

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView

class OverlayService : Service() {
    
    private var windowManager: WindowManager? = null
    private var cursorView: View? = null
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        createCursorOverlay()
    }
    
    private fun createCursorOverlay() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 0
        
        // Create cursor view (simple circle)
        cursorView = ImageView(this).apply {
            setImageResource(android.R.drawable.ic_menu_mylocation)
        }
        
        windowManager?.addView(cursorView, params)
    }
    
    fun updateCursorPosition(x: Float, y: Float) {
        cursorView?.let { view ->
            val params = view.layoutParams as WindowManager.LayoutParams
            params.x = x.toInt()
            params.y = y.toInt()
            windowManager?.updateViewLayout(view, params)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cursorView?.let {
            windowManager?.removeView(it)
        }
    }
}
