package com.xingqiao.common.rocketmq.service;

import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.messaging.Message;

/**
 * RocketMQ 消息服务接口
 * 定义消息发送的核心方法
 *
 * @author xingqiao
 * @date 2025-09-21
 */
public interface RocketMQMessageService {

    /**
     * 发送同步消息
     *
     * @param topic 主题
     * @param message 消息内容
     * @return 发送结果
     */
    <T> SendResult sendSyncMessage(String topic, T message);

    /**
     * 发送同步消息（带标签）
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @return 发送结果
     */
    <T> SendResult sendSyncMessage(String topic, String tag, T message);

    /**
     * 发送异步消息
     *
     * @param topic 主题
     * @param message 消息内容
     */
    <T> void sendAsyncMessage(String topic, T message);

    /**
     * 发送异步消息（带标签）
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     */
    <T> void sendAsyncMessage(String topic, String tag, T message);

    /**
     * 发送单向消息
     *
     * @param topic 主题
     * @param message 消息内容
     */
    <T> void sendOneWayMessage(String topic, T message);

    /**
     * 发送单向消息（带标签）
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     */
    <T> void sendOneWayMessage(String topic, String tag, T message);

    /**
     * 发送顺序消息
     *
     * @param topic 主题
     * @param message 消息内容
     * @param hashKey 用于确定消息发送到哪个队列的键值
     * @return 发送结果
     */
    <T> SendResult sendOrderlyMessage(String topic, T message, String hashKey);

    /**
     * 发送顺序消息（带标签）
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @param hashKey 用于确定消息发送到哪个队列的键值
     * @return 发送结果
     */
    <T> SendResult sendOrderlyMessage(String topic, String tag, T message, String hashKey);

    /**
     * 发送Spring Message消息
     *
     * @param destination 目标地址，格式为：topic:tag
     * @param message Spring Message对象
     * @return 发送结果
     */
    SendResult sendSpringMessage(String destination, Message<?> message);
}