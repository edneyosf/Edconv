#!/bin/bash
set -euo pipefail

source .env

BUILD_DIR="Edconv.AppDir"
APPDATA_FILE_NAME="${ID}.appdata.xml"
APPRUN_FILE="${PACKAGING_DIR}/AppRun"
USR_DIR="${BUILD_DIR}/usr"
APPLICATIONS_DIR="${USR_DIR}/share/applications"
METAINFO_DIR="${USR_DIR}/share/metainfo"
OUTPUT="${APP_NAME}-${APP_VERSION}.AppImage"
ARCH=$(uname -m)
APPIMAGE_TOOL="appimagetool-${ARCH}.AppImage"

echo "🔨 Building $ARCH release..."
./gradlew composeApp:createReleaseDistributable

echo "📥 Getting appimagetool..."
wget -nc -q https://github.com/AppImage/AppImageKit/releases/download/continuous/${APPIMAGE_TOOL}
chmod +x $APPIMAGE_TOOL

echo "📦 Creating bundle..."
rm -rf $BUILD_DIR
mkdir -p $BUILD_DIR
mkdir -p "./${APPLICATIONS_DIR}"
mkdir -p "./${METAINFO_DIR}"
cp $APPRUN_FILE $BUILD_DIR
cp -r ${RELEASE_DIR}/* "./${USR_DIR}"
cp $ICON_FILE "./${BUILD_DIR}/"
cp $DESKTOP_FILE "./${BUILD_DIR}/"
cp $DESKTOP_FILE "./${APPLICATIONS_DIR}/"
cp $METAINFO_FILE "./${METAINFO_DIR}/${APPDATA_FILE_NAME}"
ARCH=$(uname -m) ./${APPIMAGE_TOOL} --no-appstream ${BUILD_DIR} $OUTPUT

echo "🧹 Cleaning..."
rm -rf $BUILD_DIR
rm ${APPIMAGE_TOOL}

echo "Launch it -> ./${OUTPUT}"
echo "✅ Done -> $OUTPUT"