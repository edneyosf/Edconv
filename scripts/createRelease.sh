#!/bin/bash
set -euo pipefail

source .env

echo "📥 Getting gradle..."
wget -nc -q https://services.gradle.org/distributions/gradle-8.9-bin.zip -P ./gradle/wrapper/

echo "🔨 Building release..."
./gradlew composeApp:createReleaseDistributable

echo "✅ Done -> $RELEASE_DIR"