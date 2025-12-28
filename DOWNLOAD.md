# Download & Install Guide

## Quick Start

### Option 1: Build from Source (Recommended)

1. **Clone the repository:**
```bash
git clone https://github.com/crajveer1212-dotcom/hand-gesture-control-app.git
cd hand-gesture-control-app
```

2. **Download MediaPipe Model:**
   - Download: [hand_landmarker.task](https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task)
   - Place in: `app/src/main/assets/hand_landmarker.task`

3. **Open in Android Studio:**
   - File → Open → Select project folder
   - Wait for Gradle sync

4. **Build APK:**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Or run: `./gradlew assembleDebug`

5. **Install:**
   - APK location: `app/build/outputs/apk/debug/app-debug.apk`
   - Transfer to phone and install
   - Or use: `adb install app/build/outputs/apk/debug/app-debug.apk`

### Option 2: Download Pre-built APK (Coming Soon)

Check the [Releases](https://github.com/crajveer1212-dotcom/hand-gesture-control-app/releases) page for pre-built APKs.

## Requirements

- **Android Device:** Android 8.0+ (API 26+)
- **Camera:** Front-facing camera required
- **Storage:** ~50 MB free space
- **RAM:** 2 GB minimum

## First Time Setup

After installing:

1. **Grant Permissions:**
   - Camera permission
   - Overlay permission
   - Accessibility service

2. **Enable Accessibility:**
   - Settings → Accessibility
   - Find "Hand Gesture Control"
   - Toggle ON

3. **Test:**
   - Open app
   - Show ✌️ two fingers for 1 second
   - Start controlling!

## MediaPipe Model Download

The app requires the MediaPipe hand tracking model:

**Direct Download:**
```
https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task
```

**File Size:** ~10 MB

**Location:** Place in `app/src/main/assets/` before building

## Troubleshooting

### "Camera permission denied"
- Go to Settings → Apps → Hand Gesture Control → Permissions
- Enable Camera

### "Overlay permission required"
- Settings → Apps → Special app access → Display over other apps
- Enable for Hand Gesture Control

### "Accessibility service not enabled"
- Settings → Accessibility → Hand Gesture Control
- Toggle ON

### Build errors
- Ensure Android Studio is updated
- Sync Gradle: File → Sync Project with Gradle Files
- Clean build: Build → Clean Project

## Development Setup

For developers:

```bash
# Install Android Studio
# Download from: https://developer.android.com/studio

# Install JDK 11+
# Download from: https://adoptium.net/

# Clone and setup
git clone https://github.com/crajveer1212-dotcom/hand-gesture-control-app.git
cd hand-gesture-control-app

# Download MediaPipe model (see above)

# Build
./gradlew assembleDebug
```

## Support

Having issues? [Open an issue](https://github.com/crajveer1212-dotcom/hand-gesture-control-app/issues) on GitHub!
