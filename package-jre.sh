#!bin/bash

# Generate jar file and the deps info for minimal jre creation
. ./package.sh
# Remove the currently existing jre
rm -rf project-jre
# Create a new jre
jlink \
    --add-modules $(cat deps.info) \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --output ./project-jre