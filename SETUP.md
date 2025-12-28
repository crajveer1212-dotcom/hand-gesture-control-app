# Setup Instructions

## Prerequisites

1. **Android Studio** (latest version)
2. **JDK 11+**
3. **Android SDK** with API 26+ (Android 8.0)

## Step-by-Step Setup

### 1. Create New Android Project

```bash
# In Android Studio:
# File → New → New Project
# Select "Empty Activity"
# Language: Kotlin
# Minimum SDK: API 26 (Android 8.0)
# Package name: com.handgesture.control
```

### 2. Add Dependencies

Add to `app/build.gradle`:

```gradle
dependencies {
    // MediaPipe for hand tracking
    implementation 'com.google.mediapipe:tasks-vision:0.10.8'
    
    // CameraX
    def camerax_version = "1.3.1"
    implementation "androidx.camera:camera-core:${camerax_version}"
    implementation "androidx.camera:camera-camera2:${camerax_version}"
    implementation "androidx.camera:camera-lifecycle:${camerax_version}"
    implementation "androidx.camera:camera-view:${camerax_version}"
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
```

### 3. Add Permissions

Add to `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-feature android:name="android.hardware.camera.front" android:required="true" />
```

### 4. Download MediaPipe Model

1. Download `hand_landmarker.task` from:
   https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task

2. Create `assets` folder: `app/src/main/assets/`

3. Place `hand_landmarker.task` in the assets folder

### 5. Copy Source Files

Copy all `.kt` files from this repository to:
`app/src/main/java/com/handgesture/control/`

Files to copy:
- MainActivity.kt
- HandTracker.kt
- GestureDetector.kt
- HandGestureService.kt
- OverlayService.kt

### 6. Create Layout File

Create `app/src/main/res/layout/activity_main.xml` (see repository)

### 7. Build APK

```bash
# In Android Studio:
# Build → Build Bundle(s) / APK(s) → Build APK(s)

# Or via command line:
./gradlew assembleDebug

# APK will be in: app/build/outputs/apk/debug/app-debug.apk
```

### 8. Install on Device

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Project Structure

```
app/src/main/
├── java/com/handgesture/control/
│   ├── MainActivity.kt              # Main UI and camera setup
│   ├── HandTracker.kt              # MediaPipe integration
│   ├── GestureDetector.kt          # Gesture recognition logic
│   ├── HandGestureService.kt       # Accessibility service
│   └── OverlayService.kt           # Cursor overlay
├── res/
│   ├── layout/
│   │   └── activity_main.xml       # Main UI layout
│   └── values/
│       └── strings.xml
└── assets/
    └── hand_landmarker.task        # MediaPipe model
```

## Troubleshooting

### Camera not working
- Check camera permissions granted
- Ensure front camera exists
- Check camera not in use by another app

### Gestures not working
- Enable Accessibility Service in Settings
- Grant overlay permission
- Check hand is in optimal distance (8-15cm)

### High battery usage
- Ensure FPS is set to 15 (not 30)
- Check frame skip is enabled (every 3rd frame)
- Verify auto-pause is working

## Release Build

For production release:

1. Create keystore
2. Configure signing in `build.gradle`
3. Build release APK:

```bash
./gradlew assembleRelease
```

## Need Help?

Open an issue on GitHub if you encounter any problems!
