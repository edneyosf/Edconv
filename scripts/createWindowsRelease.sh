#!/bin/bash
set -euo pipefail

source .env

OUTPUT_DIR="composeApp/build/compose/binaries/main-release/exe"
OUTPUT="${OUTPUT_DIR}/${APP_NAME}-${APP_VERSION}.exe"

echo "📥 Getting gradle..."
choco install wget --no-progress
wget -nc -q https://services.gradle.org/distributions/gradle-8.9-bin.zip -P ./gradle/wrapper/

echo "🔨 Building release..."
./gradlew composeApp:packageReleaseExe

echo "✅ Done -> $OUTPUT"