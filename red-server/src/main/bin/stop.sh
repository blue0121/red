#!/bin/bash

name="red-server"

pid=`ps aux | grep $name | grep -v grep | awk '{print $2}'`
if [ -z "$pid" ]; then
	echo "$name is not running"
else
	echo "stopping $name..."
	kill $pid
fi