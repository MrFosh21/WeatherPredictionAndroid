#!/bin/sh

APP_HOME=$(cd "${0%/*}" >/dev/null 2>&1 && pwd -P)
APP_NAME="Gradle"
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

if [ ! -x "$JAVACMD" ] ; then
    echo "ERROR: JAVA_HOME is not set and no java command could be found." >&2
    exit 1
fi

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

eval "set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \"-Dorg.gradle.appname=$APP_NAME\" -classpath \"$CLASSPATH\" org.gradle.wrapper.GradleWrapperMain \"\$@\""
exec "$JAVACMD" "$@"
