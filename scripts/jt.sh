#!/bin/bash

echo "Running Javascript Tests"

npm install
./node_modules/karma/bin/karma start karma.conf.js --log-level debug --single-run
