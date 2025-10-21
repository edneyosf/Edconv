#!/bin/bash
set -euo pipefail

source .env

echo "📥 Getting gradle..."
wget -nc -q https://services.gradle.org/distributions/gradle-8.9-bin.zip -P ./gradle/wrapper/

echo "🔨 Building release..."
./gradlew composeApp:createReleaseDistributable

# -------------------------------
# Configurações do app (exemplo)
# -------------------------------
appName="${appName:-edconv}"
appVersion="${appVersion:-1.0.0}"
appDescriptionEn="${appDescriptionEn:-EdConv App}"
appDescriptionPt="${appDescriptionPt:-Aplicativo EdConv}"
appAuthor="${appAuthor:-Edney OSF}"

arch="amd64"

# Diretórios
buildDir="build/createDeb"
outputDir="build/compose/binaries/main-release/deb"
distDir="build/compose/binaries/main-release/app"

targetDir="$buildDir/opt/$appName"
shareDir="$buildDir/usr/share/applications"
desktopFile="$shareDir/$appName.desktop"
debianDir="$buildDir/DEBIAN"
controlFile="$debianDir/control"
libDir="$targetDir/lib"
runtimeLibDir="$libDir/runtime/lib"
binFile="$targetDir/bin/$appName"
outputDeb="$outputDir/${appName}_${appVersion}_${arch}.deb"

# -------------------------------
# Preparar diretórios
# -------------------------------
echo "Cleaning build and output directories..."
rm -rf "$buildDir" "$outputDir"
mkdir -p "$targetDir" "$shareDir" "$debianDir" "$outputDir" "$libDir" "$runtimeLibDir"

# -------------------------------
# Copiar arquivos do app
# -------------------------------
echo "Copying application files..."
cp -r "$distDir/"* "$targetDir/"

# -------------------------------
# Criar arquivo .desktop
# -------------------------------
echo "Creating .desktop file..."
cat > "$desktopFile" <<EOF
[Desktop Entry]
Name=${appName^}
Comment=$appDescriptionEn
Comment[pt_BR]=$appDescriptionPt
Exec=/opt/$appName/bin/$appName
Icon=/opt/$appName/lib/$appName.png
Terminal=false
Type=Application
Categories=AudioVideo;Utility;
StartupWMClass=edneyosf-edconv-MainKt
EOF

# -------------------------------
# Criar DEBIAN/control
# -------------------------------
echo "Creating DEBIAN/control file..."
cat > "$controlFile" <<EOF
Package: $appName
Version: $appVersion
Section: video
Priority: optional
Architecture: $arch
Maintainer: $appAuthor <edney.osf@gmail.com>
Description: $appDescriptionEn
EOF

# -------------------------------
# Permissões
# -------------------------------
echo "Setting execute permissions..."
chmod +x "$binFile"
chmod +x "$libDir/libapplauncher.so"
chmod +x "$runtimeLibDir/jexec"
chmod +x "$runtimeLibDir/jspawnhelper"

# -------------------------------
# Gerar pacote .deb
# -------------------------------
echo "Building .deb package..."
fakeroot dpkg-deb --build "$buildDir" "$outputDeb"

echo ".deb generated at: $outputDeb"