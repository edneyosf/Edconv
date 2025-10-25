#!/bin/bash
set -euo pipefail

source .env

OUTPUT_DIR="composeApp/build/compose/binaries/main-release/dmg"
OUTPUT="${OUTPUT_DIR}/${APP_NAME}-${APP_VERSION}.dmg"
ARCH=$(uname -m)

echo "🔨 Building $ARCH release..."
./gradlew composeApp:packageReleaseDmg

echo "✅ Done -> $OUTPUT"