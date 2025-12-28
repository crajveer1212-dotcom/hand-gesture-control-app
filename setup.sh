#!/bin/bash

# Hand Gesture Control App - Automated Setup Script
# This script automates the entire build process

echo "=================================="
echo "Hand Gesture Control App - Setup"
echo "=================================="
echo ""

# Check if Android Studio is installed
if ! command -v android &> /dev/null; then
    echo "⚠️  Android Studio not found!"
    echo ""
    echo "Please install Android Studio first:"
    echo "https://developer.android.com/studio"
    echo ""
    exit 1
fi

# Step 1: Create assets directory
echo "📁 Creating assets directory..."
mkdir -p app/src/main/assets

# Step 2: Download MediaPipe model
echo "⬇️  Downloading MediaPipe hand tracking model..."
curl -L "https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task" \
     -o app/src/main/assets/hand_landmarker.task

if [ $? -eq 0 ]; then
    echo "✅ MediaPipe model downloaded successfully!"
else
    echo "❌ Failed to download MediaPipe model"
    echo "Please download manually from:"
    echo "https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task"
    exit 1
fi

# Step 3: Build the APK
echo ""
echo "🔨 Building APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build successful!"
    echo ""
    echo "📱 APK Location:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "📲 To install on your phone:"
    echo "   1. Connect phone via USB"
    echo "   2. Enable USB debugging on phone"
    echo "   3. Run: adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "   OR"
    echo ""
    echo "   1. Copy app-debug.apk to your phone"
    echo "   2. Open it on phone"
    echo "   3. Enable 'Install from Unknown Sources' if asked"
    echo "   4. Install"
    echo ""
else
    echo "❌ Build failed!"
    echo "Please check the error messages above"
    exit 1
fi
