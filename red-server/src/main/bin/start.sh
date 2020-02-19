#!/bin/bash

BIN=$(pwd)/$0
APP_DIR=$(dirname $(dirname "${BIN}"))
LIB_DIR=$APP_DIR/lib
LOG_DIR=$APP_DIR/log

HEAP="-Xms256m -Xmx512m -Xss256k -XX:MaxMetaspaceSize=256M"
DUMP="-XX:+HeapDumpOnOutOfMemoryError"

name="red-server-1.0.0"

if [ -z "${LOG_DIR}" ]; then
  export LOG_DIR="${LOG_DIR}"
  echo "No LOG_DIR"
fi

pid()
{
	pid=$(ps aux | grep $name | grep -v grep | grep -v kill | awk '{print $2}')
}

start()
{
  pid
  if [ -z "${pid}" ]; then
    java ${HEAP} ${DUMP} -jar ${LIB_DIR}/${name}.jar > ${APP_DIR}/output 2>&1 &
    echo "starting $name..., pid is $!"
  else
    echo "${name} is running, pid is ${pid}"
  fi
}

stop()
{
  pid
  if [ -z "${pid}" ]; then
    echo "${name} is not running"
    exit 0
  else
    echo "stopping ${name}..., pid is ${pid}"
    kill ${pid}
  fi

  sleep 5

  pid
  if [ -z "${pid}" ]; then
    echo "${name} is stopped"
  else
    echo "kill ${name}..., pid is ${pid}"
    kill -9 ${pid}
  fi
}

kills()
{
  pid
  if [ -z "${pid}" ]; then
    echo "${name} is not running"
  else
    echo "kill ${name}..., pid is ${pid}"
    kill -9 ${pid}
  fi
}

status()
{
  pid
  if [ -z "${pid}" ]; then
    echo "${name} is not running"
  else
    echo "${name} is running, pid is ${pid}"
    grep 'Threads' /proc/${pid}/status
  fi
}

case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "kill")
    kills
    ;;
  *)
    echo "Usage: ./start.sh [start|stop|status|kill]"
    exit 1
    ;;
esac
