#!/bin/bash
# Compile and run all JUnit tests. Usage: ./run-tests.sh
set -e
cd "$(dirname "$0")"
CP="lib/junit-4.13.2.jar:lib/hamcrest-all-1.3.jar"
rm -rf build && mkdir -p build
javac -d build -cp "$CP" src/*.java test/*.java
java -cp "build:$CP" org.junit.runner.JUnitCore AccountTest SocialNetworkTest
