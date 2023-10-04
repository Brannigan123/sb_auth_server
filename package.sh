#!bin/bash

# Build the jar
mvn clean package -DskipTests -Dmaven.javadoc.skip=true
# Extract the jar
cd target && mkdir -p auth_server-latest && cd auth_server-latest
jar xf ../auth_server-latest.jar
# Generate the deps info
cd ../..
jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 21  \
    --print-module-deps  \
    --class-path 'target/auth_server-latest/BOOT-INF/lib/*'  \
    target/auth_server-latest.jar > deps.info