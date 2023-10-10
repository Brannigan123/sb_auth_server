#!/bin/bash

# Set the path to the OpenAPI specification
if [ -z "$1" ]; then
    SPEC_PATH="http://localhost:8080/v3/api-docs"
else
  SPEC_PATH="$1"
fi

# Remove preexisting markdown docs
rm -rf Apis/*
rm -rf Models/*

# Generate markdown documentation from OpenAPI spec
openapi-generator generate \
-g markdown \
-i $SPEC_PATH \
-o .