#!/bin/bash

#main函数所在类（类全称）
MAIN_CLASS=com.baifendian.comp.rpc.RpcApplication

cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

PIDS=`ps  --no-heading -C java -f --width 1000 |grep "$CONF_DIR"| grep "$MAIN_CLASS" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The service already started!"
    echo "PID: $PIDS"
    exit 1
fi


LOGS_DIR=$DEPLOY_DIR/logs
if [ ! -d $LOGS_DIR ]; then
	mkdir $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/stdout.log


LIB_JARS=$DEPLOY_DIR/lib/*

JAVA_OPTS=" -Xloggc:gc_memory_logs.log -Xmx16g -Xms4g  -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "

echo -e "Starting the service ...\c"

echo "nohup java $JAVA_OPTS -Ddruid.logType=slf4j -classpath $CONF_DIR:$LIB_JARS $MAIN_CLASS > $STDOUT_FILE 2>&1 &"

nohup java $JAVA_OPTS -Ddruid.logType=slf4j -classpath $CONF_DIR:$LIB_JARS $MAIN_CLASS > $STDOUT_FILE 2>&1 &

echo "OK!"
PIDS=`ps  --no-heading -C java -f --width 1000 | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"