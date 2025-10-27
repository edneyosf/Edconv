@echo off
set "OUTPUT_DIR=composeApp\build\compose\binaries\main-release\exe"
set "OUTPUT=%OUTPUT_DIR%\%APP_NAME%-%APP_VERSION%.exe"
set "ARCH=%PROCESSOR_ARCHITECTURE%"

echo 🔨 Building %ARCH% release...
gradlew.bat composeApp:packageReleaseExe

echo ✅ Done -> %OUTPUT%
pause