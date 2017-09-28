#!/bin/sh

if [[ -f $APPNAME-$VERSION.jar ]]; then
    java -Duser.timezone=America/Sao_Paulo $JAVA_OPTS -jar $APPNAME-$VERSION.jar
else
    java -Duser.timezone=America/Sao_Paulo $JAVA_OPTS -jar feeds-admin-api-*.jar
fi
