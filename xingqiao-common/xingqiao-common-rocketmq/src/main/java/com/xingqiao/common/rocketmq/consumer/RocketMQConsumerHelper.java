package com.xingqiao.common.rocketmq.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * RocketMQ 消费者助手类
 * 提供消息消费的工具方法
 *
 * @author xingqiao
 * @date 2025-09-21
 */
public class RocketMQConsumerHelper {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQConsumerHelper.class);

    /**
     * 安全地处理消息
     * 包含异常捕获和日志记录
     *
     * @param message 消息对象
     * @param messageHandler 消息处理器
     */
    public static void safeHandleMessage(MessageExt message, Consumer<MessageExt> messageHandler) {
        try {
            logger.info("收到消息: topic={}, tags={}, keys={}, msgId={}, bodyLength={}",
                    message.getTopic(), message.getTags(), message.getKeys(), message.getMsgId(), message.getBody().length);
            messageHandler.accept(message);
            logger.info("消息处理成功: msgId={}", message.getMsgId());
        } catch (Exception e) {
            logger.error("消息处理失败: msgId={}, topic={}, tags={}", message.getMsgId(), message.getTopic(), message.getTags(), e);
            throw new RuntimeException("消息处理失败: " + message.getMsgId(), e);
        }
    }

    /**
     * 从消息中提取字符串内容
     *
     * @param message 消息对象
     * @return 消息内容字符串
     */
    public static String getMessageBodyAsString(MessageExt message) {
        if (message == null || message.getBody() == null) {
            return null;
        }
        return new String(message.getBody(), StandardCharsets.UTF_8);
    }

    /**
     * 检查消息是否重复
     * 可以根据实际业务需求实现更复杂的重复消息判断逻辑
     *
     * @param message 消息对象
     * @return 是否为重复消息
     */
    public static boolean isDuplicateMessage(MessageExt message) {
        // 这里可以实现消息去重逻辑，例如基于消息ID或业务键
        // 简单实现：如果重试次数大于0，则认为可能是重复消息
        return message.getReconsumeTimes() > 0;
    }

    /**
     * 构建完整的消费者组名
     * 可以根据业务需求添加前缀或后缀
     *
     * @param baseGroupName 基础组名
     * @return 完整的消费者组名
     */
    public static String buildConsumerGroupName(String baseGroupName) {
        if (StringUtils.isEmpty(baseGroupName)) {
            throw new IllegalArgumentException("Consumer group name cannot be empty");
        }
        // 可以根据环境、项目等信息添加前缀
        return baseGroupName;
    }

    /**
     * 计算消息延迟时间
     * 用于消息重试策略
     *
     * @param reconsumeTimes 重试次数
     * @return 延迟时间（毫秒）
     */
    public static long calculateDelayTime(int reconsumeTimes) {
        // 简单的指数退避策略
        // 重试次数越多，延迟时间越长
        switch (reconsumeTimes) {
            case 0:
                return 1000 * 60; // 1分钟
            case 1:
                return 1000 * 60 * 5; // 5分钟
            case 2:
                return 1000 * 60 * 10; // 10分钟
            case 3:
                return 1000 * 60 * 30; // 30分钟
            case 4:
                return 1000 * 60 * 60 * 2; // 2小时
            default:
                return 1000 * 60 * 60 * 24; // 24小时
        }
    }
}