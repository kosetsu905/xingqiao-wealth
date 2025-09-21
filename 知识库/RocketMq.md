# 安装RocketMq

wget https://archive.apache.org/dist/rocketmq/5.2.0/rocketmq-all-5.2.0-bin-release.zip

如果没有安装unzip，请安装：
sudo apt install unzip

unzip rocketmq-all-5.2.0-bin-release.zip
cd rocketmq-all-5.2.0-bin-release


# 修改 NameServer 内存配置
sed -i 's/-Xms1g -Xmx1g -Xmn1g/-Xms512m -Xmx512m -Xmn256m/g' bin/runserver.sh
# 修改 Broker 内存配置
sed -i 's/-Xms1g -Xmx1g -Xmn1g/-Xms512m -Xmx512m -Xmn512m/g' bin/runbroker.sh


# 启动 NameServer
nohup sh bin/mqnamesrv &

# 查看启动日志
tail -f ~/logs/rocketmqlogs/namesrv.log


# 启动 broker
# 使用指定配置文件启动 Broker
nohup sh bin/mqbroker -c conf/broker_public.conf &

# 查看启动日志
tail -f ~/logs/rocketmqlogs/broker.log


# 停止 Broker
sh bin/mqshutdown broker
# 停止 namesrv
sh bin/mqshutdown namesrv

jps -l


# 停止所有 RocketMQ 相关进程
pkill -f NamesrvStartup
pkill -f BrokerStartup



#查看日志

tail -n 20 ~/logs/rocketmqlogs/broker.log
tail -n 20 ~/logs/rocketmqlogs/namesrv.log



#验证是否安装成功
