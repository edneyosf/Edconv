#!/bin/bash

wget -nc -q https://services.gradle.org/distributions/gradle-8.9-bin.zip -P ./gradle/wrapper/
./gradlew composeApp:packageReleaseDmg