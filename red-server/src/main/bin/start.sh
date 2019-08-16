#!/bin/bash

HEAP="-Xms256m -Xmx512m -Xss256k -XX:MaxMetaspaceSize=256M"
DUMP="-XX:+HeapDumpOnOutOfMemoryError"

name="red-server"

pid=`ps aux | grep $name | grep -v grep | awk '{print $2}'`
if [ -z "$pid" ]; then
	echo "starting $name..."
	java $HEAP $DUMP -jar $name-1.0.jar > output.out &
else
	echo "$name is running"
fi
