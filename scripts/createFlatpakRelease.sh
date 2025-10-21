#!/bin/bash
set -euo pipefail

source .env

YAML_PATH="${PACKAGING_DIR}/${ID}.json"
REPO_DIR="repo"
BUILD_DIR="build-dir"
BUILDER_DIR=".flatpak-builder"
OUTPUT="${APP_NAME}-${APP_VERSION}-x86_64.flatpak"

echo "🔨 Building Flatpak for ${ID}..."
mkdir -p "$REPO_DIR" "$BUILD_DIR"
flatpak-builder --force-clean --repo="$REPO_DIR" "$BUILD_DIR" "$YAML_PATH"

echo "📦 Creating bundle ${OUTPUT}..."
flatpak build-bundle "$REPO_DIR" "$OUTPUT" "$ID"

echo "🧹 Cleaning..."
rm -rf $REPO_DIR
rm -rf $BUILD_DIR
rm -rf $BUILDER_DIR

echo "Run to install -> flatpak --user install $OUTPUT"
echo "Launch it -> flatpak run $ID"
echo "✅ Done -> $OUTPUT"