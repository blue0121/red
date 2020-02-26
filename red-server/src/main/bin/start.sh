#!/bin/bash

BIN=$(pwd)/$0
APP_DIR=$(dirname $(dirname "${BIN}"))
LIB_DIR=${APP_DIR}/lib
CONF_DIR=${APP_DIR}/conf

HEAP="-Xms256m -Xmx512m -Xss256k -XX:MaxMetaspaceSize=256M"
DUMP="-XX:+HeapDumpOnOutOfMemoryError"

module="red.server"
main_class="blue.red.server.ServerMain"

if [ -z "${LOG_DIR}" ]; then
  export LOG_DIR=${APP_DIR}/log
fi

pid()
{
	pid=$(ps aux | grep ${module} | grep -v grep | grep -v kill | awk '{print $2}')
}

start()
{
  pid
  if [ -z "${pid}" ]; then
    java ${HEAP} ${DUMP} -cp ${CONF_DIR} -p ${LIB_DIR} -m ${module}/${main_class} > ${APP_DIR}/output 2>&1 &
    echo "starting ${module}..., pid is $!"
  else
    echo "${module} is running, pid is ${pid}"
  fi
}

startup()
{
  pid
  if [ -z "${pid}" ]; then
    java ${HEAP} ${DUMP} -cp ${CONF_DIR} -p ${LIB_DIR} -m ${module}/${main_class} 2>&1
    echo "starting ${module}..., pid is $!"
  else
    echo "${module} is running, pid is ${pid}"
  fi
}

stop()
{
  pid
  if [ -z "${pid}" ]; then
    echo "${module} is not running"
    exit 0
  else
    echo "stopping ${module}..., pid is ${pid}"
    kill ${pid}
  fi

  sleep 5

  pid
  if [ -z "${pid}" ]; then
    echo "${module} is stopped"
  else
    echo "kill ${module}..., pid is ${pid}"
    kill -9 ${pid}
  fi
}

kills()
{
  pid
  if [ -z "${pid}" ]; then
    echo "${module} is not running"
  else
    echo "kill ${module}..., pid is ${pid}"
    kill -9 ${pid}
  fi
}

status()
{
  pid
  if [ -z "${pid}" ]; then
    echo "${module} is not running"
  else
    echo "${module} is running, pid is ${pid}"
    grep 'Threads' /proc/${pid}/status
  fi
}

case "$1" in
  "start")
    start
    ;;
  "startup")
    startup
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
    echo "Usage: ./start.sh [start|startup|stop|status|kill]"
    exit 1
    ;;
esac
