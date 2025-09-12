#!/bin/bash

# 星桥跨境投资平台 test 环境启动脚本

# 设置应用名称和环境
MODULE_NAME=${1:-"xingqiao-auth"}
PROFILE="test"

# 设置 JVM 参数
JAVA_OPTS="-Xms512m -Xmx1024m"

# 获取脚本所在目录
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
PROJECT_DIR=$(dirname "$SCRIPT_DIR")

# 进入项目目录
cd "$PROJECT_DIR"

# 创建日志目录
mkdir -p logs

echo "Starting $MODULE_NAME with profile: $PROFILE"

# 查找对应的 JAR 文件
JAR_FILE=$(find . -path "./$MODULE_NAME/target/*.jar" -type f | head -1)

if [ -z "$JAR_FILE" ]; then
    # 如果在 target 目录找不到，则在 docker 目录查找
    JAR_FILE=$(find ./docker -name "*.jar" | grep "$MODULE_NAME" | head -1)
fi

if [ -z "$JAR_FILE" ]; then
    echo "Error: No JAR file found for $MODULE_NAME"
    exit 1
fi

echo "Found JAR file: $JAR_FILE"

# 检查是否已经运行
PID=$(ps -ef | grep "$MODULE_NAME" | grep java | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
    echo "$MODULE_NAME is already running with PID: $PID"
    exit 1
fi

# 启动应用
echo "Starting application..."
nohup java $JAVA_OPTS -jar "$JAR_FILE" --spring.profiles.active=$PROFILE > "logs/$MODULE_NAME.log" 2>&1 &

# 等待应用启动
sleep 3

# 检查是否启动成功
PID=$(ps -ef | grep "$MODULE_NAME" | grep java | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
    echo "SUCCESS: $MODULE_NAME started with PID: $PID"
    echo "Logs are available at: logs/$MODULE_NAME.log"
else
    echo "ERROR: Failed to start $MODULE_NAME. Check logs/$MODULE_NAME.log for details."
    exit 1
fi