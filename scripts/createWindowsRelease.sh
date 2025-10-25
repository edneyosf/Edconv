#!/bin/bash
set -euo pipefail

source .env

OUTPUT_DIR="composeApp/build/compose/binaries/main-release/exe"
OUTPUT="${OUTPUT_DIR}/${APP_NAME}-${APP_VERSION}.exe"
ARCH=$PROCESSOR_ARCHITECTURE

echo "🔨 Building $ARCH release..."
./gradlew composeApp:packageReleaseExe

echo "✅ Done -> $OUTPUT"