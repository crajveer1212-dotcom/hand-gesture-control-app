#!/bin/bash

echo "=========================================="
echo "Hand Gesture Control App - Docker Build"
echo "=========================================="
echo ""

# Build Docker image
echo "Building Docker image..."
docker build -t hand-gesture-app .

# Create output directory
mkdir -p output

# Run container and extract APK
echo "Building APK..."
docker run --rm -v $(pwd)/output:/output hand-gesture-app

echo ""
echo "=========================================="
echo "BUILD COMPLETE!"
echo "=========================================="
echo ""
echo "APK Location: output/app-debug.apk"
echo ""
echo "Install on your Android device:"
echo "1. Transfer output/app-debug.apk to your phone"
echo "2. Enable 'Install from Unknown Sources'"
echo "3. Install the APK"
echo ""
