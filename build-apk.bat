@echo off
echo Building Hand Gesture Control APK...
echo.

REM Check if gradlew exists
if not exist gradlew.bat (
    echo Error: gradlew.bat not found!
    echo Please run this from the project root directory.
    pause
    exit /b 1
)

REM Build the APK
call gradlew.bat assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo BUILD SUCCESSFUL!
    echo ========================================
    echo.
    echo APK Location:
    echo app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo You can now install this APK on your Android device!
    echo.
) else (
    echo.
    echo ========================================
    echo BUILD FAILED!
    echo ========================================
    echo.
    echo Please check the error messages above.
    echo.
)

pause
