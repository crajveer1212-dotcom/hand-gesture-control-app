# Quick Start Guide - No Android Studio Required!

## Easiest Method: Use the Automated Script

### For Linux/Mac:

```bash
# 1. Clone the repository
git clone https://github.com/crajveer1212-dotcom/hand-gesture-control-app.git
cd hand-gesture-control-app

# 2. Make script executable
chmod +x setup.sh

# 3. Run the script (does everything automatically!)
./setup.sh
```

### For Windows:

```bash
# 1. Clone the repository
git clone https://github.com/crajveer1212-dotcom/hand-gesture-control-app.git
cd hand-gesture-control-app

# 2. Run the script (does everything automatically!)
setup.bat
```

**The script will:**
- ✅ Download the MediaPipe model automatically
- ✅ Build the APK for you
- ✅ Tell you where to find the APK

---

## Manual Method (If Script Doesn't Work)

### Step 1: Install Android Studio
Download from: https://developer.android.com/studio

### Step 2: Clone Repository
```bash
git clone https://github.com/crajveer1212-dotcom/hand-gesture-control-app.git
```

### Step 3: Download MediaPipe Model
1. Download: [hand_landmarker.task](https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task)
2. Create folder: `app/src/main/assets/`
3. Place the file there

### Step 4: Open in Android Studio
- File → Open → Select project folder
- Wait for Gradle sync (5 minutes)

### Step 5: Build APK
- Build → Build Bundle(s) / APK(s) → Build APK(s)
- Wait 2-3 minutes
- APK created at: `app/build/outputs/apk/debug/app-debug.apk`

### Step 6: Install on Phone

**Option A: USB Cable**
```bash
# Enable USB debugging on phone first
adb install app/build/outputs/apk/debug/app-debug.apk
```

**Option B: Manual Transfer**
1. Copy `app-debug.apk` to your phone (via USB, email, cloud, etc.)
2. Open the APK file on your phone
3. Enable "Install from Unknown Sources" if prompted
4. Tap Install

---

## After Installation

1. **Open the app**
2. **Grant permissions:**
   - Camera ✅
   - Overlay ✅
   - Accessibility Service ✅

3. **Test it:**
   - Show ✌️ two fingers for 1 second
   - Start controlling with your finger!

---

## Troubleshooting

### "I don't have Android Studio"
Use the automated script! It uses command-line tools only.

### "Script failed"
Install Android SDK command-line tools:
https://developer.android.com/studio#command-tools

### "Can't install APK on phone"
- Settings → Security → Enable "Unknown Sources"
- Or Settings → Apps → Special Access → Install Unknown Apps

### "Build failed"
Make sure you have:
- JDK 11+ installed
- Internet connection (for downloading dependencies)

---

## Need Help?

Open an issue: https://github.com/crajveer1212-dotcom/hand-gesture-control-app/issues
