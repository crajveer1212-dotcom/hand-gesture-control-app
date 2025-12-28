package com.handgesture.control

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    
    private lateinit var previewView: PreviewView
    private lateinit var statusText: TextView
    private lateinit var toggleButton: Button
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var handTracker: HandTracker
    private lateinit var gestureDetector: GestureDetector
    
    private var isTrackingEnabled = false
    private var camera: Camera? = null
    
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
        private const val REQUEST_OVERLAY_PERMISSION = 101
        private const val REQUEST_ACCESSIBILITY = 102
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        previewView = findViewById(R.id.previewView)
        statusText = findViewById(R.id.statusText)
        toggleButton = findViewById(R.id.toggleButton)
        
        cameraExecutor = Executors.newSingleThreadExecutor()
        handTracker = HandTracker(this)
        gestureDetector = GestureDetector()
        
        toggleButton.setOnClickListener {
            toggleTracking()
        }
        
        checkPermissions()
    }
    
    private fun checkPermissions() {
        val permissions = mutableListOf<String>()
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }
        
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            checkOverlayPermission()
        }
    }
    
    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        } else {
            checkAccessibilityPermission()
        }
    }
    
    private fun checkAccessibilityPermission() {
        if (!isAccessibilityServiceEnabled()) {
            Toast.makeText(
                this,
                "Please enable Accessibility Service in Settings",
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivityForResult(intent, REQUEST_ACCESSIBILITY)
        } else {
            startCamera()
        }
    }
    
    private fun isAccessibilityServiceEnabled(): Boolean {
        val service = "${packageName}/.HandGestureService"
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return enabledServices?.contains(service) == true
    }
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetFrameRate(Range(15, 15)) // 15 FPS for battery saving
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, HandAnalyzer())
                }
            
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
                
                updateStatus("Camera ready. Show ✌️ two fingers to activate")
            } catch (e: Exception) {
                updateStatus("Camera error: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }
    
    private fun toggleTracking() {
        isTrackingEnabled = !isTrackingEnabled
        
        if (isTrackingEnabled) {
            startOverlayService()
            toggleButton.text = "Stop Tracking"
            updateStatus("Tracking ACTIVE - Point with finger")
        } else {
            stopOverlayService()
            toggleButton.text = "Start Tracking"
            updateStatus("Tracking STOPPED")
        }
    }
    
    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        startService(intent)
    }
    
    private fun stopOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        stopService(intent)
    }
    
    private fun updateStatus(message: String) {
        runOnUiThread {
            statusText.text = message
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkOverlayPermission()
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        when (requestCode) {
            REQUEST_OVERLAY_PERMISSION -> {
                if (Settings.canDrawOverlays(this)) {
                    checkAccessibilityPermission()
                } else {
                    Toast.makeText(this, "Overlay permission required", Toast.LENGTH_LONG).show()
                }
            }
            REQUEST_ACCESSIBILITY -> {
                if (isAccessibilityServiceEnabled()) {
                    startCamera()
                }
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        stopOverlayService()
    }
    
    private inner class HandAnalyzer : ImageAnalysis.Analyzer {
        private var frameCount = 0
        
        @androidx.camera.core.ExperimentalGetImage
        override fun analyze(imageProxy: ImageProxy) {
            frameCount++
            if (frameCount % 3 != 0) {
                imageProxy.close()
                return
            }
            
            if (!isTrackingEnabled) {
                imageProxy.close()
                return
            }
            
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                handTracker.processFrame(mediaImage) { result ->
                    when (result.gestureType) {
                        GestureType.ACTIVATION -> {
                            updateStatus("✌️ Activation detected!")
                        }
                        GestureType.DEACTIVATION -> {
                            updateStatus("✊ Deactivation detected!")
                        }
                        GestureType.POINT -> {
                            if (result.fingerPosition != null) {
                                val action = gestureDetector.detectAction(
                                    result.fingerPosition,
                                    result.gestureType
                                )
                                handleGestureAction(action)
                            }
                        }
                        GestureType.ZOOM -> {
                            if (result.zoomDistance != null) {
                                val action = gestureDetector.detectAction(
                                    Pair(0f, 0f),
                                    result.gestureType,
                                    result.zoomDistance
                                )
                                handleGestureAction(action)
                            }
                        }
                        GestureType.NONE -> {
                            updateStatus("⏸️ Auto-paused (no hand)")
                        }
                    }
                }
            }
            
            imageProxy.close()
        }
    }
    
    private fun handleGestureAction(action: GestureDetector.GestureAction) {
        when (action.type) {
            GestureDetector.ActionType.MOVE_CURSOR -> {
                updateStatus("👆 Pointing mode")
            }
            GestureDetector.ActionType.TAP -> {
                updateStatus("✅ Tap detected!")
            }
            GestureDetector.ActionType.SWIPE -> {
                updateStatus("↔️ Swipe ${action.direction}")
            }
            GestureDetector.ActionType.ZOOM_IN -> {
                updateStatus("🔍 Zoom IN")
            }
            GestureDetector.ActionType.ZOOM_OUT -> {
                updateStatus("🔍 Zoom OUT")
            }
            GestureDetector.ActionType.NONE -> {}
        }
    }
}
