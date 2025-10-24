#!/bin/bash
set -euo pipefail

source .env

OUTPUT_DIR="composeApp/build/compose/binaries/main-release/deb"
OUTPUT="${OUTPUT_DIR}/${APP_NAME}-${APP_VERSION}-x86_64.deb"
USR_DIR="${OUTPUT_DIR}/usr"
OPT_DIR="${OUTPUT_DIR}/opt"
DEBIAN_DIR="${OUTPUT_DIR}/DEBIAN"
APPLICATIONS_DIR="${USR_DIR}/share/applications"
DESKTOP_DEB_FILE="${METADATA_DIR}/edconv.desktop"

echo "🔨 Building release..."
./gradlew composeApp:createReleaseDistributable

rm -rf $OUTPUT_DIR
mkdir -p $USR_DIR
mkdir -p $OPT_DIR
mkdir -p $DEBIAN_DIR
mkdir -p $APPLICATIONS_DIR
cp $DESKTOP_DEB_FILE "${APPLICATIONS_DIR}/"
cp -r $RELEASE_DIR $OPT_DIR
cat > "${OUTPUT_DIR}/DEBIAN/control" <<EOF
Package: edconv
Version: $APP_VERSION
Section: video
Priority: optional
Architecture: amd64
Maintainer: $APP_AUTHOR <$APP_EMAIL>
Description: $APP_DESCRIPTION_EN
Homepage: $APP_HOMEPAGE
EOF

fakeroot dpkg-deb --build "$OUTPUT_DIR" test.deb