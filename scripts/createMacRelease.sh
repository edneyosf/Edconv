#!/bin/bash
set -euo pipefail

source .env

OUTPUT_DIR="composeApp/build/compose/binaries/main-release/dmg"
OUTPUT="${OUTPUT_DIR}/${APP_NAME}-${APP_VERSION}.dmg"

echo "📥 Getting gradle..."
wget -nc -q https://services.gradle.org/distributions/gradle-8.9-bin.zip -P ./gradle/wrapper/

echo "🔨 Building release..."
./gradlew composeApp:packageReleaseDmg

echo "✅ Done -> $OUTPUT"