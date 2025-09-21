package com.xingqiao.order.rocketmq.consumer;

import com.alibaba.fastjson2.JSON;
import com.xingqiao.common.rocketmq.consumer.RocketMQConsumerHelper;
import com.xingqiao.order.rocketmq.domain.OrderMessage;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订单创建消息消费者
 * 用于接收和处理订单创建消息
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Component
@RocketMQMessageListener(topic = "order_topic", selectorExpression = "order_created", consumerGroup = "order_created_group")
public class OrderCreatedConsumer implements RocketMQListener<MessageExt> {

    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    @Override
    public void onMessage(MessageExt message) {
        RocketMQConsumerHelper.safeHandleMessage(message, msg -> {
            // 获取消息体内容
            String messageBody = RocketMQConsumerHelper.getMessageBodyAsString(msg);
            logger.info("收到订单创建消息，内容: {}", messageBody);
            
            // 解析消息内容为OrderMessage对象
            OrderMessage orderMessage = JSON.parseObject(messageBody, OrderMessage.class);
            
            // 处理订单创建逻辑
            processOrderCreated(orderMessage);
        });
    }

    /**
     * 处理订单创建逻辑
     * 这里可以实现订单创建后的业务处理，如记录日志、更新状态等
     */
    private void processOrderCreated(OrderMessage orderMessage) {
        logger.info("开始处理订单创建，订单ID: {}, 客户ID: {}, 订单金额: {}",
                orderMessage.getOrderId(),
                orderMessage.getCustomerId(),
                orderMessage.getAmount());
        
        // 这里可以添加实际的业务处理逻辑
        // 例如：更新订单状态、发送通知、记录操作日志等
        
        logger.info("订单创建处理完成，订单ID: {}", orderMessage.getOrderId());
    }
}