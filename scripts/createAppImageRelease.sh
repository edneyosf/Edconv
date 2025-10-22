#!/bin/bash
set -euo pipefail

source .env

BUILD_DIR="Edconv.AppDir"
APPDATA_FILE_NAME="${ID}.appdata.xml"
APPRUN_FILE="${PACKAGING_DIR}/AppRun"
USR_DIR="${BUILD_DIR}/usr"
APPLICATIONS_DIR="${USR_DIR}/share/applications"
METAINFO_DIR="${USR_DIR}/share/metainfo"
OUTPUT="${APP_NAME}-${APP_VERSION}-x86_64.AppImage"

echo "📥 Getting gradle..."
wget -nc -q https://services.gradle.org/distributions/gradle-8.9-bin.zip -P ./gradle/wrapper/

echo "🔨 Building release..."
./gradlew composeApp:createReleaseDistributable

echo "📥 Getting appimagetool..."
wget -nc -q https://github.com/AppImage/AppImageKit/releases/download/continuous/appimagetool-x86_64.AppImage
chmod +x appimagetool-x86_64.AppImage

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
ARCH=x86_64 ./appimagetool-x86_64.AppImage --no-appstream ${BUILD_DIR} $OUTPUT

echo "🧹 Cleaning..."
rm -rf $BUILD_DIR

echo "Launch it -> ./${OUTPUT}"
echo "✅ Done -> $OUTPUT"