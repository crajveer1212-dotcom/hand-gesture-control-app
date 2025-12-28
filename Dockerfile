FROM ubuntu:22.04

# Install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    wget \
    unzip \
    git \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install Android SDK
ENV ANDROID_SDK_ROOT=/opt/android-sdk
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    cd ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip && \
    unzip commandlinetools-linux-9477386_latest.zip && \
    rm commandlinetools-linux-9477386_latest.zip && \
    mv cmdline-tools latest

ENV PATH=${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools

# Accept licenses
RUN yes | sdkmanager --licenses

# Install required SDK components
RUN sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Download MediaPipe model
RUN mkdir -p app/src/main/assets && \
    curl -L "https://storage.googleapis.com/mediapipe-models/hand_landmarker/hand_landmarker/float16/latest/hand_landmarker.task" \
         -o app/src/main/assets/hand_landmarker.task

# Make gradlew executable
RUN chmod +x gradlew || true

# Build APK
RUN ./gradlew assembleDebug --no-daemon --stacktrace || \
    (gradle wrapper --gradle-version 8.2 && ./gradlew assembleDebug --no-daemon --stacktrace)

# Output location
CMD ["cp", "app/build/outputs/apk/debug/app-debug.apk", "/output/"]
