#!/bin/bash

# 星桥跨境投资平台 Linux 启动脚本
# 支持启动不同模块并指定不同环境

# 默认设置
DEFAULT_MODULE="xingqiao-auth"
DEFAULT_PROFILE="test"
JAVA_OPTS="-Xms512m -Xmx1024m"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 打印帮助信息
print_usage() {
    echo "Usage: $0 [start|stop|restart|status] [module] [profile]"
    echo "  module:   xingqiao-auth (default), xingqiao-gateway, xingqiao-system, etc."
    echo "  profile:  test (default), dev, local"
    echo ""
    echo "Examples:"
    echo "  $0 start                            # Start auth module with test profile"
    echo "  $0 start xingqiao-gateway dev       # Start gateway with dev profile"
    echo "  $0 stop xingqiao-auth               # Stop auth module"
    echo "  $0 restart xingqiao-system test     # Restart system module with test profile"
}

# 获取脚本所在目录
get_script_dir() {
    cd "$(dirname "${BASH_SOURCE[0]}")" && pwd
}

# 获取应用PID
get_pid() {
    local module=$1
    ps -ef | grep $module | grep -v grep | awk '{print $2}'
}

# 启动应用
start_app() {
    local module=$1
    local profile=$2
    
    # 检查是否已经运行
    pid=$(get_pid $module)
    if [ -n "$pid" ]; then
        echo -e "${YELLOW}$module is already running (pid: $pid)${NC}"
        return 1
    fi
    
    # 创建日志目录
    mkdir -p logs
    
    # 查找 JAR 文件
    JAR_FILE=$(find . -path "./$module/target/*.jar" -type f | head -1)
    if [ -z "$JAR_FILE" ]; then
        echo -e "${RED}No JAR file found for $module!${NC}"
        return 1
    fi
    
    echo -e "${GREEN}Starting $module with profile: $profile${NC}"
    echo "JAVA_OPTS: $JAVA_OPTS"
    echo "JAR_FILE: $JAR_FILE"
    
    # 启动应用
    nohup java $JAVA_OPTS -jar $JAR_FILE --spring.profiles.active=$profile > logs/${module}.log 2>&1 &
    
    # 等待应用启动
    sleep 5
    
    # 检查是否启动成功
    pid=$(get_pid $module)
    if [ -n "$pid" ]; then
        echo -e "${GREEN}$module started successfully (pid: $pid)${NC}"
        echo "Logs are written to logs/${module}.log"
        return 0
    else
        echo -e "${RED}$module failed to start. Please check logs/${module}.log for details${NC}"
        return 1
    fi
}

# 停止应用
stop_app() {
    local module=$1
    pid=$(get_pid $module)
    
    if [ -n "$pid" ]; then
        echo -e "${YELLOW}Stopping $module (pid: $pid)${NC}"
        kill $pid
        
        # 等待应用停止
        for i in {1..10}; do
            if ! ps -p $pid > /dev/null; then
                echo -e "${GREEN}$module stopped successfully${NC}"
                return 0
            fi
            sleep 1
        done
        
        # 强制杀死进程
        echo -e "${YELLOW}Force killing $module${NC}"
        kill -9 $pid
        echo -e "${GREEN}$module force killed${NC}"
        return 0
    else
        echo -e "${YELLOW}$module is not running${NC}"
        return 1
    fi
}

# 查看应用状态
status_app() {
    local module=$1
    pid=$(get_pid $module)
    
    if [ -n "$pid" ]; then
        echo -e "${GREEN}$module is running (pid: $pid)${NC}"
        return 0
    else
        echo -e "${YELLOW}$module is not running${NC}"
        return 1
    fi
}

# 主逻辑
main() {
    local action=${1:-"start"}
    local module=${2:-$DEFAULT_MODULE}
    local profile=${3:-$DEFAULT_PROFILE}
    
    case "$action" in
        start)
            start_app $module $profile
            ;;
        stop)
            stop_app $module
            ;;
        restart)
            stop_app $module
            sleep 2
            start_app $module $profile
            ;;
        status)
            status_app $module
            ;;
        *)
            print_usage
            exit 1
            ;;
    esac
}

# 执行主逻辑
main "$@"