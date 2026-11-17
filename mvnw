#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper Unix Script
# ----------------------------------------------------------------------------

if which mvn >/dev/null 2>&1; then
    exec mvn "$@"
fi

if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME is not set" >&2
    exit 1
fi

exec "$JAVA_HOME/bin/java" -classpath .mvn/wrapper/maven-wrapper.jar \
    "-Dmaven.multiModuleProjectDirectory=$PWD" \
    org.apache.maven.wrapper.MavenWrapperMain "$@"
