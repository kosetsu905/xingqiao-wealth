package com.xingqiao.order.rocketmq.producer;

import com.xingqiao.common.rocketmq.producer.RocketMQProducerHelper;
import com.xingqiao.order.rocketmq.domain.OrderMessage;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单消息生产者
 * 用于在代码中直接发送订单相关消息
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Component
public class OrderMessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderMessageProducer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 订单主题名称
    private static final String ORDER_TOPIC = "order_topic";

    /**
     * 发送订单消息
     *
     * @param tag 标签
     * @param orderMessage 订单消息
     * @return 是否发送成功
     */
    public boolean sendOrderMessage(String tag, OrderMessage orderMessage) {
        try {
            logger.info("开始发送订单消息，订单ID: {}, 标签: {}", orderMessage.getOrderId(), tag);
            
            // 构建目标地址
            String destination = ORDER_TOPIC + (tag != null ? ":" + tag : "");
            
            // 发送同步消息
            SendResult sendResult = rocketMQTemplate.syncSend(destination, orderMessage);
            
            // 记录发送结果日志
            RocketMQProducerHelper.logSendResult(ORDER_TOPIC, sendResult);
            
            // 验证发送结果
            boolean isSuccess = RocketMQProducerHelper.isSendSuccess(sendResult);
            logger.info("订单消息发送{}{}，订单ID: {}", 
                    isSuccess ? "成功" : "失败", 
                    isSuccess ? "" : "，状态: " + sendResult.getSendStatus(),
                    orderMessage.getOrderId());
            
            return isSuccess;
        } catch (Exception e) {
            logger.error("订单消息发送异常，订单ID: {}, 标签: {}", orderMessage.getOrderId(), tag, e);
            return false;
        }
    }

    /**
     * 发送订单创建消息
     */
    public boolean sendOrderCreatedMessage(OrderMessage orderMessage) {
        return sendOrderMessage("order_created", orderMessage);
    }

    /**
     * 发送订单支付消息
     */
    public boolean sendOrderPaidMessage(OrderMessage orderMessage) {
        return sendOrderMessage("order_paid", orderMessage);
    }

    /**
     * 发送订单完成消息
     */
    public boolean sendOrderCompletedMessage(OrderMessage orderMessage) {
        return sendOrderMessage("order_completed", orderMessage);
    }

    /**
     * 发送订单取消消息
     */
    public boolean sendOrderCancelledMessage(OrderMessage orderMessage) {
        return sendOrderMessage("order_cancelled", orderMessage);
    }

    /**
     * 发送订单异常消息
     */
    public boolean sendOrderErrorMessage(OrderMessage orderMessage, String errorMsg) {
        // 将错误信息添加到备注
        if (orderMessage.getRemark() == null) {
            orderMessage.setRemark(errorMsg);
        } else {
            orderMessage.setRemark(orderMessage.getRemark() + "; " + errorMsg);
        }
        return sendOrderMessage("order_error", orderMessage);
    }
}