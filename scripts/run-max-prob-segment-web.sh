#!/bin/bash

cd `dirname 0`/max-prob-segment-web
java -Dfile.encoding=UTF-8 -jar target/dependency/webapp-runner.jar --port 8080 target/max-prob-segment-web-1.0-SNAPSHOT.war
cd -
