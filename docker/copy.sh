#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/ry_20250523.sql ./mysql/db
cp ../sql/ry_config_20250224.sql ./mysql/db

# copy html
echo "begin copy html "
cp -r ../xingqiao-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy xingqiao-gateway "
cp ../xingqiao-gateway/target/xingqiao-gateway.jar ./xingqiao/gateway/jar

echo "begin copy xingqiao-auth "
cp ../xingqiao-auth/target/xingqiao-auth.jar ./xingqiao/auth/jar

echo "begin copy xingqiao-visual "
cp ../xingqiao-visual/xingqiao-monitor/target/xingqiao-visual-monitor.jar  ./xingqiao/visual/monitor/jar

echo "begin copy xingqiao-modules-system "
cp ../xingqiao-modules/xingqiao-system/target/xingqiao-modules-system.jar ./xingqiao/modules/system/jar

echo "begin copy xingqiao-modules-file "
cp ../xingqiao-modules/xingqiao-file/target/xingqiao-modules-file.jar ./xingqiao/modules/file/jar

echo "begin copy xingqiao-modules-job "
cp ../xingqiao-modules/xingqiao-job/target/xingqiao-modules-job.jar ./xingqiao/modules/job/jar

echo "begin copy xingqiao-modules-gen "
cp ../xingqiao-modules/xingqiao-gen/target/xingqiao-modules-gen.jar ./xingqiao/modules/gen/jar

