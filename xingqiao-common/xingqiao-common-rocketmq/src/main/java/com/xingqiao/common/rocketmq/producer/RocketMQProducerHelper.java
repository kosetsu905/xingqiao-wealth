package com.xingqiao.common.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * RocketMQ 生产者助手类
 * 提供消息生产的工具方法
 *
 * @author xingqiao
 * @date 2025-09-21
 */
public class RocketMQProducerHelper {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducerHelper.class);

    /**
     * 创建RocketMQ消息
     *
     * @param topic 主题
     * @param body 消息体
     * @return Message对象
     */
    public static Message createMessage(String topic, String body) {
        return new Message(topic, body.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建带标签的RocketMQ消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param body 消息体
     * @return Message对象
     */
    public static Message createMessage(String topic, String tag, String body) {
        return new Message(topic, tag, body.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建带标签和键的RocketMQ消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param keys 消息键
     * @param body 消息体
     * @return Message对象
     */
    public static Message createMessage(String topic, String tag, String keys, String body) {
        Message message = new Message(topic, tag, body.getBytes(StandardCharsets.UTF_8));
        message.setKeys(keys);
        return message;
    }

    /**
     * 生成唯一的消息ID
     *
     * @return 唯一消息ID
     */
    public static String generateMessageId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 验证发送结果
     *
     * @param sendResult 发送结果
     * @return 是否发送成功
     */
    public static boolean isSendSuccess(SendResult sendResult) {
        if (sendResult == null) {
            return false;
        }
        switch (sendResult.getSendStatus()) {
            case SEND_OK:
                return true;
            case FLUSH_DISK_TIMEOUT:
            case FLUSH_SLAVE_TIMEOUT:
            case SLAVE_NOT_AVAILABLE:
            default:
                return false;
        }
    }

    /**
     * 记录发送结果日志
     *
     * @param topic 主题
     * @param sendResult 发送结果
     */
    public static void logSendResult(String topic, SendResult sendResult) {
        if (sendResult == null) {
            logger.warn("消息发送结果为空，topic: {}", topic);
            return;
        }
        logger.info("消息发送成功，topic: {}, msgId: {}, queueId: {}, offset: {}",
                topic,
                sendResult.getMsgId(),
                sendResult.getMessageQueue().getQueueId(),
                sendResult.getQueueOffset());
    }

    /**
     * 构建Spring Message
     *
     * @param payload 消息体
     * @param <T> 消息体类型
     * @return Spring Message对象
     */
    public static <T> org.springframework.messaging.Message<T> buildSpringMessage(T payload) {
        return MessageBuilder.withPayload(payload).build();
    }

    /**
     * 构建带消息ID的Spring Message
     *
     * @param payload 消息体
     * @param messageId 消息ID
     * @param <T> 消息体类型
     * @return Spring Message对象
     */
    public static <T> org.springframework.messaging.Message<T> buildSpringMessageWithId(T payload, String messageId) {
        return MessageBuilder.withPayload(payload)
                .setHeader("MESSAGE_ID", messageId)
                .build();
    }

    /**
     * 构建完整的生产者组名
     * 可以根据业务需求添加前缀或后缀
     *
     * @param baseGroupName 基础组名
     * @return 完整的生产者组名
     */
    public static String buildProducerGroupName(String baseGroupName) {
        if (baseGroupName == null || baseGroupName.isEmpty()) {
            throw new IllegalArgumentException("Producer group name cannot be empty");
        }
        // 可以根据环境、项目等信息添加前缀
        return baseGroupName;
    }
}