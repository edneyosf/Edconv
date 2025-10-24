#!/bin/bash
set -euo pipefail

source .env

echo "🔨 Building release..."
./gradlew composeApp:createReleaseDistributable

echo "✅ Done -> $RELEASE_DIR"