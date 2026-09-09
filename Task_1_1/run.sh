#!/bin/bash

rm -rf out docs app.jar

SOURCES=$(find ./src/main/java -name "*.java")

javac -d out $SOURCES

javadoc -d docs -sourcepath ./src/main/java -subpackages org.example

jar cfe app.jar org.example.Main -C out .


java -jar app.jar

