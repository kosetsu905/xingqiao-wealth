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
 * 订单支付消息消费者
 * 用于接收和处理订单支付消息
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Component
@RocketMQMessageListener(topic = "order_topic", selectorExpression = "order_paid", consumerGroup = "order_paid_group")
public class OrderPaidConsumer implements RocketMQListener<MessageExt> {

    private static final Logger logger = LoggerFactory.getLogger(OrderPaidConsumer.class);

    @Override
    public void onMessage(MessageExt message) {
        RocketMQConsumerHelper.safeHandleMessage(message, msg -> {
            // 获取消息体内容
            String messageBody = RocketMQConsumerHelper.getMessageBodyAsString(msg);
            logger.info("收到订单支付消息，内容: {}", messageBody);
            
            // 解析消息内容为OrderMessage对象
            OrderMessage orderMessage = JSON.parseObject(messageBody, OrderMessage.class);
            
            // 处理订单支付逻辑
            processOrderPaid(orderMessage);
        });
    }

    /**
     * 处理订单支付逻辑
     * 这里可以实现订单支付后的业务处理，如更新库存、发送通知等
     */
    private void processOrderPaid(OrderMessage orderMessage) {
        logger.info("开始处理订单支付，订单ID: {}, 客户ID: {}, 支付金额: {}",
                orderMessage.getOrderId(),
                orderMessage.getCustomerId(),
                orderMessage.getAmount());
        
        // 这里可以添加实际的业务处理逻辑
        // 例如：更新订单状态为已支付、扣减库存、记录财务流水等
        
        logger.info("订单支付处理完成，订单ID: {}", orderMessage.getOrderId());
    }
}