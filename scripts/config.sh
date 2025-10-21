#!/bin/bash

export ID="io.github.edneyosf.edconv"
export APP_NAME="edconv"
export APP_VERSION="1.4.1"
export APP_DESCRIPTION_EN="An intuitive FFmpeg front-end for converting movies, shows and music"
export APP_DESCRIPTION_PT="Uma interface intuitiva para FFmpeg para converter filmes, séries e músicas"
export APP_AUTHOR="Edney Osf"
export METADATA_DIR="./scripts/metadata"
export PACKAGING_DIR="./scripts/packaging"
export DESKTOP_FILE="${METADATA_DIR}/${ID}.desktop"
export METAINFO_FILE="${METADATA_DIR}/${ID}.metainfo.xml"
export ICON_FILE="./assets/${ID}.png"
export RELEASE_DIR="./composeApp/build/compose/binaries/main-release/app/edconv"