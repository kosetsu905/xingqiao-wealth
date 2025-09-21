package com.xingqiao.common.rocketmq.example;

import com.xingqiao.common.rocketmq.service.RocketMQMessageService;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RocketMQ 使用示例类
 * 展示如何在其他服务中使用rocketmq模块的功能
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Component
public class RocketMQExample {

    @Autowired
    private RocketMQMessageService rocketMQMessageService;

    /**
     * 示例：发送同步消息
     *
     * @param topic 主题
     * @param message 消息内容
     * @return 发送结果
     */
    public SendResult sendSyncMessageExample(String topic, String message) {
        return rocketMQMessageService.sendSyncMessage(topic, message);
    }

    /**
     * 示例：发送带标签的同步消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @return 发送结果
     */
    public SendResult sendSyncMessageWithTagExample(String topic, String tag, String message) {
        return rocketMQMessageService.sendSyncMessage(topic, tag, message);
    }

    /**
     * 示例：发送异步消息
     *
     * @param topic 主题
     * @param message 消息内容
     */
    public void sendAsyncMessageExample(String topic, String message) {
        rocketMQMessageService.sendAsyncMessage(topic, message);
    }

    /**
     * 示例：发送单向消息
     *
     * @param topic 主题
     * @param message 消息内容
     */
    public void sendOneWayMessageExample(String topic, String message) {
        rocketMQMessageService.sendOneWayMessage(topic, message);
    }

    /**
     * 示例：发送顺序消息
     *
     * @param topic 主题
     * @param message 消息内容
     * @param hashKey 用于确定消息发送到哪个队列的键值
     * @return 发送结果
     */
    public SendResult sendOrderlyMessageExample(String topic, String message, String hashKey) {
        return rocketMQMessageService.sendOrderlyMessage(topic, message, hashKey);
    }

    /**
     * 示例：发送对象消息
     * RocketMQMessageService支持发送任意对象，会自动转换为JSON
     *
     * @param topic 主题
     * @param object 消息对象
     * @return 发送结果
     */
    public SendResult sendObjectMessageExample(String topic, Object object) {
        return rocketMQMessageService.sendSyncMessage(topic, object);
    }
}

/**
 * 示例消息实体类
 */
class ExampleMessage {
    private String id;
    private String content;
    private long timestamp;

    public ExampleMessage() {
    }

    public ExampleMessage(String id, String content) {
        this.id = id;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ExampleMessage{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}