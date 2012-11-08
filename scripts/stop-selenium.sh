#!/bin/bash

PID=`ps -ef | grep selenium | grep -v "grep" | awk '{print $2}'`
kill -9 $PID
