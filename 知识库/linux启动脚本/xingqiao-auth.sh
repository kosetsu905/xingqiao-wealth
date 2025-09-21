#!/bin/sh
## ==============================================
## 模块启动脚本 - 适用于 xingqiao-auth模块
## ==============================================

# Java 环境配置
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk  # 修改为实际的 Java 安装路径
export JRE_HOME=$JAVA_HOME/jre
export PATH=$JAVA_HOME/bin:$PATH

# 模块配置
MODULE_NAME="xingqiao-auth"         # 模块名称
MODULE_DIR="/home/xingqiao/$MODULE_NAME"       # 模块目录
JAR_NAME="$MODULE_NAME.jar"                    # Jar 文件名
PID_FILE="$MODULE_DIR/$MODULE_NAME.pid"         # PID 文件路径
LOG_FILE="$MODULE_DIR/$MODULE_NAME.log"         # 日志文件路径

# 使用说明
usage() {
    echo "Usage: sh $0 [start|stop|restart|status]"
    exit 1
}

# 检查程序是否在运行
is_exist() {
    if [ -f "$PID_FILE" ]; then
        pid=$(cat "$PID_FILE")
        if ps -p "$pid" > /dev/null; then
            return 0  # 运行中
        else
            rm -f "$PID_FILE"  # 清理无效 PID 文件
        fi
    fi
    return 1  # 未运行
}

# 启动方法
start() {
    is_exist
    if [ $? -eq 0 ]; then
        echo ">>> $MODULE_NAME is already running PID=$(cat $PID_FILE) <<<"
        return 0
    fi

    # 创建日志目录
    mkdir -p "$(dirname "$LOG_FILE")"

    # 启动命令
    nohup $JRE_HOME/bin/java \
        -Xms256m -Xmx512m \
        -Dspring.profiles.active=test \
        -jar "$MODULE_DIR/$JAR_NAME" > "$LOG_FILE" 2>&1 &

    # 记录 PID
    echo $! > "$PID_FILE"
    echo ">>> $MODULE_NAME started PID=$! <<<"
    echo ">>> Logs: $LOG_FILE <<<"
}

# 停止方法
stop() {
    is_exist
    if [ $? -eq 0 ]; then
        pid=$(cat "$PID_FILE")
        echo ">>> Stopping $MODULE_NAME (PID=$pid) <<<"
        kill "$pid"

        # 等待进程停止
        for i in {1..10}; do
            if ! ps -p "$pid" > /dev/null; then
                break
            fi
            sleep 1
        done

        # 强制终止如果仍在运行
        if ps -p "$pid" > /dev/null; then
            echo ">>> Force stopping $MODULE_NAME (PID=$pid) <<<"
            kill -9 "$pid"
        fi

        rm -f "$PID_FILE"
        echo ">>> $MODULE_NAME stopped <<<"
    else
        echo ">>> $MODULE_NAME is not running <<<"
    fi
}

# 输出运行状态
status() {
    is_exist
    if [ $? -eq 0 ]; then
        pid=$(cat "$PID_FILE")
        echo ">>> $MODULE_NAME is running PID=$pid <<<"
        echo ">>> Memory usage: $(ps -p $pid -o rss=) KB <<<"
        echo ">>> Last log entries:"
        tail -n 5 "$LOG_FILE"
    else
        echo ">>> $MODULE_NAME is not running <<<"
    fi
}

# 重启
restart() {
    stop
    sleep 2
    start
}

# 根据输入参数执行对应方法
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
    "restart")
        restart
        ;;
    *)
        usage
        ;;
esac
exit 0