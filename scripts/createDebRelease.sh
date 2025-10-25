#!/bin/bash
set -euo pipefail

source .env

OUTPUT_DIR="composeApp/build/compose/binaries/main-release/deb"
OUTPUT="${APP_NAME}-${APP_VERSION}.deb"
USR_DIR="${OUTPUT_DIR}/usr"
OPT_DIR="${OUTPUT_DIR}/opt"
DEBIAN_DIR="${OUTPUT_DIR}/DEBIAN"
APPLICATIONS_DIR="${USR_DIR}/share/applications"
ARCH=$(dpkg-architecture -qDEB_HOST_ARCH)

echo "🔨 Building $ARCH release..."
./gradlew composeApp:createReleaseDistributable

echo "📦 Creating bundle..."
rm -rf $OUTPUT_DIR
mkdir -p $USR_DIR
mkdir -p $OPT_DIR
mkdir -p $DEBIAN_DIR
mkdir -p $APPLICATIONS_DIR
cp -r $RELEASE_DIR $OPT_DIR
cat > "${APPLICATIONS_DIR}/edconv.desktop" <<EOF
[Desktop Entry]
Name=$APP_NAME
Comment=$APP_DESCRIPTION_EN
Comment[pt_BR]=$APP_DESCRIPTION_PT
Exec=/opt/${APP_NAME}/bin/${APP_NAME}
Icon=/opt/${APP_NAME}/lib/${APP_NAME}.png
Type=Application
Categories=Utility
StartupWMClass=edneyosf-edconv-MainKt
EOF
cat > "${DEBIAN_DIR}/control" <<EOF
Package: edconv
Version: $APP_VERSION
Section: video
Priority: optional
Architecture: $ARCH
Maintainer: $APP_AUTHOR <$APP_EMAIL>
Description: $APP_DESCRIPTION_EN
Homepage: $APP_HOMEPAGE
EOF
fakeroot dpkg-deb --build "$OUTPUT_DIR" $OUTPUT

echo "Run to install -> sudo apt install ./$OUTPUT"
echo "✅ Done -> $OUTPUT"