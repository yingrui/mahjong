#!/bin/bash

SeleniumServer = $1
SeleniumPort = $2
Browser = $3
RootUrl = $4
Path = $5

cd test/javascript-test/
mvn clean install
cd -
cd test/javascript-test/target
unzip javascript-test-1.0-SNAPSHOT-bundle.zip
cd -
cd test/javascript-test/target/javascript-test-1.0-SNAPSHOT

echo "java -jar lib/javascript-test.jar $Selenium $SeleniumPort $Browser $RootUrl $Path"
#java -jar lib/javascript-test.jar $Selenium $SeleniumPort $Browser $RootUrl $Path
