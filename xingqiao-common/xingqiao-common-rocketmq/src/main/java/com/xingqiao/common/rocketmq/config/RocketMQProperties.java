package com.xingqiao.common.rocketmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RocketMQ 配置属性类
 * 用于从配置文件中读取RocketMQ相关配置
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {

    /**
     * NameServer地址，格式为ip:port;ip:port
     */
    private String nameServer;

    /**
     * 生产者组名
     */
    private String producerGroup;

    /**
     * 消费者组名
     */
    private String consumerGroup;

    /**
     * 发送消息超时时间，单位毫秒
     */
    private int sendMessageTimeout = 3000;

    /**
     * 消息体最大大小，单位字节
     */
    private int maxMessageSize = 4 * 1024 * 1024; // 默认4MB

    /**
     * 失败重试次数
     */
    private int retryTimesWhenSendFailed = 2;

    /**
     * 消费超时时间，单位分钟
     */
    private int consumeTimeout = 15;
    
    /**
     * ACL访问控制的AccessKey
     * 当RocketMQ服务器启用ACL时需要配置
     */
    private String accessKey;
    
    /**
     * ACL访问控制的SecretKey
     * 当RocketMQ服务器启用ACL时需要配置
     */
    private String secretKey;

    // Getters and Setters
    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public int getSendMessageTimeout() {
        return sendMessageTimeout;
    }

    public void setSendMessageTimeout(int sendMessageTimeout) {
        this.sendMessageTimeout = sendMessageTimeout;
    }

    public int getMaxMessageSize() {
        return maxMessageSize;
    }

    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public int getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    public int getConsumeTimeout() {
        return consumeTimeout;
    }

    public void setConsumeTimeout(int consumeTimeout) {
        this.consumeTimeout = consumeTimeout;
    }
    
    public String getAccessKey() {
        return accessKey;
    }
    
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}