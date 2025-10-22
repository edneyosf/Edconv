#!/bin/bash
set -euo pipefail

source .env

OUTPUT_DIR="composeApp/build/compose/binaries/main-release/rpm"
OUTPUT="${OUTPUT_DIR}/${APP_NAME}-${APP_VERSION}-1.x86_64.rpm"

echo "📥 Getting gradle..."
wget -nc -q https://services.gradle.org/distributions/gradle-8.9-bin.zip -P ./gradle/wrapper/

echo "🔨 Building release..."
./gradlew composeApp:packageReleaseRpm

echo "✅ Done -> $OUTPUT"