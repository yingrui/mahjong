#!/bin/bash

echo "Running Javascript Tests"

NODE_JS=`which npm`

if [ -n "$NODE_JS" ]
then
  npm install
  ./node_modules/karma/bin/karma start karma.conf.js --log-level debug --single-run
fi
