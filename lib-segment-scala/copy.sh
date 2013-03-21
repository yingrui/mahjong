#!/bin/bash

From="../lib-segment"

# copy from source

find ../lib-segment/src -type d | sed -e "s/.*lib-segment\///g" | xargs -n 1 mkdir -p

find ../lib-segment/src -type f | sed -e "s/.*lib-segment\///g" | xargs -n 1 -I '{}' echo "../lib-segment/{} {}" | xargs -n 2 cp

mv src/main/java src/main/scala
mv src/test/java src/test/scala

# rename java files to scala files

find src -name "*.java" | xargs -n 1 -I '{}' echo "{} {}" | sed -e "s/\.java$/\.scala/g" | xargs -n 2 mv
