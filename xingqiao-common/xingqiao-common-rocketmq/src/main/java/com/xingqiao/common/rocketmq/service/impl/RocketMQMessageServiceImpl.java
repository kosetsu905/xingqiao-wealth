package com.xingqiao.common.rocketmq.service.impl;

import com.alibaba.fastjson2.JSON;
import com.xingqiao.common.rocketmq.service.RocketMQMessageService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * RocketMQ 消息服务实现类
 * 使用RocketMQTemplate实现各种消息发送功能
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Service
public class RocketMQMessageServiceImpl implements RocketMQMessageService {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQMessageServiceImpl.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public <T> SendResult sendSyncMessage(String topic, T message) {
        try {
            logger.info("开始发送同步消息到topic: {}", topic);
            String messageStr = convertToString(message);
            SendResult result = rocketMQTemplate.syncSend(topic, messageStr);
            logger.info("同步消息发送成功，结果: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("同步消息发送失败，topic: {}", topic, e);
            throw new RuntimeException("同步消息发送失败", e);
        }
    }

    @Override
    public <T> SendResult sendSyncMessage(String topic, String tag, T message) {
        String destination = buildDestination(topic, tag);
        return sendSyncMessage(destination, message);
    }

    @Override
    public <T> void sendAsyncMessage(String topic, T message) {
        try {
            logger.info("开始发送异步消息到topic: {}", topic);
            String messageStr = convertToString(message);
            rocketMQTemplate.asyncSend(topic, messageStr, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    logger.info("异步消息发送成功，结果: {}", sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    logger.error("异步消息发送失败，topic: {}", topic, e);
                }
            });
        } catch (Exception e) {
            logger.error("异步消息发送失败，topic: {}", topic, e);
            throw new RuntimeException("异步消息发送失败", e);
        }
    }

    @Override
    public <T> void sendAsyncMessage(String topic, String tag, T message) {
        String destination = buildDestination(topic, tag);
        sendAsyncMessage(destination, message);
    }

    @Override
    public <T> void sendOneWayMessage(String topic, T message) {
        try {
            logger.info("开始发送单向消息到topic: {}", topic);
            String messageStr = convertToString(message);
            rocketMQTemplate.sendOneWay(topic, messageStr);
            logger.info("单向消息发送完成");
        } catch (Exception e) {
            logger.error("单向消息发送失败，topic: {}", topic, e);
            throw new RuntimeException("单向消息发送失败", e);
        }
    }

    @Override
    public <T> void sendOneWayMessage(String topic, String tag, T message) {
        String destination = buildDestination(topic, tag);
        sendOneWayMessage(destination, message);
    }

    @Override
    public <T> SendResult sendOrderlyMessage(String topic, T message, String hashKey) {
        try {
            logger.info("开始发送顺序消息到topic: {}", topic);
            String messageStr = convertToString(message);
            SendResult result = rocketMQTemplate.syncSendOrderly(topic, messageStr, hashKey);
            logger.info("顺序消息发送成功，结果: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("顺序消息发送失败，topic: {}", topic, e);
            throw new RuntimeException("顺序消息发送失败", e);
        }
    }

    @Override
    public <T> SendResult sendOrderlyMessage(String topic, String tag, T message, String hashKey) {
        String destination = buildDestination(topic, tag);
        return sendOrderlyMessage(destination, message, hashKey);
    }

    @Override
    public SendResult sendSpringMessage(String destination, Message<?> message) {
        try {
            logger.info("开始发送Spring Message到destination: {}", destination);
            SendResult result = rocketMQTemplate.syncSend(destination, message);
            logger.info("Spring Message发送成功，结果: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Spring Message发送失败，destination: {}", destination, e);
            throw new RuntimeException("Spring Message发送失败", e);
        }
    }

    /**
     * 将消息对象转换为字符串
     * 如果是字符串直接返回，否则转换为JSON字符串
     */
    private <T> String convertToString(T message) {
        if (message instanceof String) {
            return (String) message;
        }
        return JSON.toJSONString(message);
    }

    /**
     * 构建目标地址，格式为topic:tag
     */
    private String buildDestination(String topic, String tag) {
        return topic + (tag != null && !tag.isEmpty() ? ":" + tag : "");
    }
}