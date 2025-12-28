# Hand Gesture Control App 👆

Control your Android phone with hand gestures using the front camera!

## ✨ Features

- 👆 **Point to tap/select** - Hold finger still for 1 second
- ↔️ **Swipe gestures** - Scroll, next/previous
- 🤏 **Pinch zoom** - Thumb + index finger (automatic)
- ✌️ **Activation gesture** - Two fingers to enable
- ⏸️ **Auto-pause** - When hand not visible or out of range
- 🔋 **Battery optimized** - 15 FPS, process every 3rd frame
- 📴 **Works offline** - No internet needed
- 🎯 **Front camera only** - Lower power consumption

## 🎮 How to Use

### Activation
- ✌️ Show **TWO fingers** for 1 second → Enable tracking
- ✊ Close **fist** → Disable tracking

### Single Finger Mode
- 👆 Point with index finger → Move cursor
- Hold still 1 sec → Auto-tap
- Swipe right → Next/Scroll right
- Swipe left → Previous/Scroll left
- Swipe up/down → Scroll page

### Zoom Mode (Automatic)
- 🤌 Show thumb + index finger → Zoom mode activates
- Move fingers apart → Zoom IN
- Move fingers closer → Zoom OUT
- Hide thumb → Return to pointing mode

### Safety Features
- Auto-pauses when finger disappears
- Auto-pauses when too close (<8cm) or too far (>50cm)
- Auto-pauses when screen is off

## 📏 Optimal Distance
- **Ideal:** 8-15 cm from camera
- **Good:** 20-30 cm from camera

## 🔋 Battery Optimization
- Camera runs at 15 FPS (not 30)
- Processes every 3rd frame only
- Auto-stops when screen idle
- Front camera only (lower power)
- Single hand detection

## 🛠️ Setup Instructions

See [SETUP.md](SETUP.md) for complete build instructions.

## 📱 Compatibility
- Android 8.0+ (API 26+)
- Front-facing camera required
- Works on all apps that support touch/zoom

## 🔐 Permissions Required
- **Camera:** For hand tracking
- **Accessibility Service:** For simulating taps and gestures
- **Overlay:** For showing cursor on screen

## 📦 Tech Stack
- **MediaPipe Hands:** Real-time hand landmark detection
- **CameraX:** Efficient camera handling
- **Kotlin:** Modern Android development
- **Accessibility Service:** System-wide gesture control

## 📄 License
MIT License - Feel free to use and modify!
