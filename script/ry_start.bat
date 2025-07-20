@echo off
chcp 65001 > nul
title Nacos & Redis 启动管理器

:: --------------- 配置区域（请修改为您的实际路径）---------------
set NACOS_DIR="D:\tools\nacos\bin"
set REDIS_DIR="D:\tools\Redis-x64-3.0.504"
:: -------------------------------------------------------------

:: 启动Nacos（在新窗口）
echo [INFO] 正在启动Nacos服务...
cd /d %NACOS_DIR%
start "Nacos-Server" cmd /k "startup.cmd -m standalone && echo Nacos启动完成，按任意键关闭此窗口... && pause > nul"

:: 等待Nacos初始化
timeout /t 5 > nul

:: 启动Redis（在新窗口）
echo [INFO] 正在启动Redis服务...
cd /d %REDIS_DIR%
start "Redis-Server" cmd /k "redis-server.exe redis.windows.conf && echo Redis运行中...按Ctrl+C停止服务 && pause > nul"

echo [SUCCESS] 服务已启动！
echo Nacos控制台: http://localhost:8848/nacos
echo.
echo 按任意键关闭此启动管理器...
pause > nul