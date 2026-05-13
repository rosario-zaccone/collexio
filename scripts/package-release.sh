#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

VERSION="${COLLEXIO_VERSION:-$(sed -n '0,/<version>/{s:.*<version>\(.*\)</version>.*:\1:p}' pom.xml)}"
JAR_NAME="collexio-${VERSION}.jar"
RELEASE_INPUT="target/release-input"

mvn -q clean package

mkdir -p dist
cp "target/${JAR_NAME}" "dist/${JAR_NAME}"
sha256sum "dist/${JAR_NAME}" > "dist/${JAR_NAME}.sha256"

if command -v jpackage >/dev/null 2>&1; then
    rm -rf "$RELEASE_INPUT"
    mkdir -p "$RELEASE_INPUT"
    cp "target/${JAR_NAME}" "$RELEASE_INPUT/${JAR_NAME}"
    rm -f "dist/collexio_${VERSION}_"*.deb
    jpackage \
        --type deb \
        --name collexio \
        --app-version "$VERSION" \
        --vendor "org.collexio" \
        --description "Desktop collection manager" \
        --input "$RELEASE_INPUT" \
        --main-jar "$JAR_NAME" \
        --main-class org.collexio.bootstrap.Main \
        --dest dist \
        --java-options "-Dfile.encoding=UTF-8"
fi
