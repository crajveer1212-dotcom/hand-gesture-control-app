@echo off
REM Hand Gesture Control App - Automated Setup Script for Windows
REM This script automates the entire build process

echo ==================================
echo Hand Gesture Control App - Setup
echo ==================================
echo.

REM Step 1: Create assets directory
echo Creating assets directory...
if not exist "app\src\main\assets" mkdir app\src\main\assets

REM Step 2: Download MediaPipe model
echo Downloading MediaPipe hand tracking model...
powershell -Command "Invoke-WebRequest -Uri 'https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task' -OutFile 'app\src\main\assets\hand_landmarker.task'"

if %ERRORLEVEL% EQU 0 (
    echo MediaPipe model downloaded successfully!
) else (
    echo Failed to download MediaPipe model
    echo Please download manually from:
    echo https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task
    pause
    exit /b 1
)

REM Step 3: Build the APK
echo.
echo Building APK...
call gradlew.bat assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo.
    echo APK Location:
    echo    app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo To install on your phone:
    echo    1. Connect phone via USB
    echo    2. Enable USB debugging on phone
    echo    3. Run: adb install app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo    OR
    echo.
    echo    1. Copy app-debug.apk to your phone
    echo    2. Open it on phone
    echo    3. Enable 'Install from Unknown Sources' if asked
    echo    4. Install
    echo.
) else (
    echo Build failed!
    echo Please check the error messages above
    pause
    exit /b 1
)

pause
