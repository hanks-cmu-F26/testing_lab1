#!/bin/bash
# Compile and run every *Test class in test/. Usage: ./run-tests.sh
set -e
cd "$(dirname "$0")"
rm -rf build && mkdir -p build
javac -d build -cp "lib/*" src/*.java test/*.java
java -cp "build:lib/*" org.junit.runner.JUnitCore \
$(ls test/*Test.java | xargs -n1 basename | sed 's/\.java$//')
