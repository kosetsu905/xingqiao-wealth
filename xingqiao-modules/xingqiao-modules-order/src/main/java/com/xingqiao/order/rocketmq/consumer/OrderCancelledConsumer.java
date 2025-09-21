package com.xingqiao.order.rocketmq.consumer;

import com.xingqiao.common.rocketmq.consumer.RocketMQConsumerHelper;
import com.xingqiao.order.rocketmq.domain.OrderMessage;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 订单取消消息消费者
 * 用于处理订单取消后的业务逻辑
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Component
@RocketMQMessageListener(
        topic = "order_topic",
        selectorExpression = "order_cancelled",
        consumerGroup = "order_cancelled_group")
public class OrderCancelledConsumer implements RocketMQListener<MessageExt> {

    private static final Logger logger = LoggerFactory.getLogger(OrderCancelledConsumer.class);

    @Override
    public void onMessage(MessageExt messageExt) {
        RocketMQConsumerHelper.safeHandleMessage(messageExt, this::processMessageExt);
    }
    
    private void processMessageExt(MessageExt messageExt) {
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        logger.info("收到订单取消消息，内容: {}", body);
        // 这里可以添加实际的业务处理逻辑
    }

    /**
     * 处理订单取消消息的业务逻辑
     */
    private void processOrderCancelledMessage(OrderMessage orderMessage) {
        logger.info("开始处理订单取消消息，订单ID: {}", orderMessage.getOrderId());
        
        try {
            // 这里可以添加订单取消后的业务逻辑
            // 例如：更新订单状态、恢复库存、退款处理、发送通知等
            
            // 模拟业务处理
            logger.info("订单取消处理成功，订单ID: {}, 客户ID: {}, 订单金额: {}",
                    orderMessage.getOrderId(),
                    orderMessage.getCustomerId(),
                    orderMessage.getAmount());
            
        } catch (Exception e) {
            logger.error("订单取消处理异常，订单ID: {}", orderMessage.getOrderId(), e);
            // 可以根据具体业务需求决定是否抛出异常，触发重试机制
            // 如果需要重试，可以抛出异常；否则记录日志并返回
            // throw new RuntimeException("订单取消处理失败", e);
        }
    }
}