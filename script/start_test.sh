#!/bin/bash

# 星桥跨境投资平台 test 环境启动脚本

# 设置 JAVA_OPTS
JAVA_OPTS="-Xms512m -Xmx1024m"

# 获取脚本所在目录
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
cd $SCRIPT_DIR/..

# 设置应用名称和端口
APP_NAME=xingqiao
ACTIVE_PROFILE=test

# 检查是否已经运行
pid=`ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]; then
    echo "$APP_NAME is already running (pid: $pid)"
    exit 1
fi

# 创建日志目录
mkdir -p logs

# 启动应用
echo "Starting $APP_NAME with profile: $ACTIVE_PROFILE"
echo "JAVA_OPTS: $JAVA_OPTS"

# 查找 JAR 文件并启动
JAR_FILE=$(find . -name "*.jar" | head -1)
if [ -z "$JAR_FILE" ]; then
    echo "No JAR file found!"
    exit 1
fi

nohup java $JAVA_OPTS -jar $JAR_FILE --spring.profiles.active=$ACTIVE_PROFILE > logs/start.log 2>&1 &

# 检查是否启动成功
sleep 5
pid=`ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}'`
if [ -n "$pid" ]; then
    echo "$APP_NAME started successfully (pid: $pid)"
    echo "Logs are written to logs/start.log"
else
    echo "$APP_NAME failed to start. Please check logs/start.log for details"
    exit 1
fi