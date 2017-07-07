#!/bin/bash
./gradlew clean bintray -PpublishBuildType=debug
./gradlew clean bintray -PpublishBuildType=release
