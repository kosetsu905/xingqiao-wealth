package com.xingqiao.order.rocketmq.service.impl;

import com.xingqiao.common.rocketmq.service.RocketMQMessageService;
import com.xingqiao.order.rocketmq.domain.OrderMessage;
import com.xingqiao.order.rocketmq.service.OrderMessageService;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单消息服务实现类
 * 实现订单消息发送的具体逻辑
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Service
public class OrderMessageServiceImpl implements OrderMessageService {

    private static final Logger logger = LoggerFactory.getLogger(OrderMessageServiceImpl.class);

    @Autowired
    private RocketMQMessageService rocketMQMessageService;

    // 订单主题名称
    private static final String ORDER_TOPIC = "order_topic";
    // 订单标签名称
    private static final String ORDER_CREATED_TAG = "order_created";
    private static final String ORDER_PAID_TAG = "order_paid";
    private static final String ORDER_COMPLETED_TAG = "order_completed";
    private static final String ORDER_CANCELLED_TAG = "order_cancelled";
    private static final String ORDER_ERROR_TAG = "order_error";

    @Override
    public SendResult sendOrderCreatedMessage(OrderMessage orderMessage) {
        logger.info("开始发送订单创建消息，订单ID: {}", orderMessage.getOrderId());
        try {
            SendResult result = rocketMQMessageService.sendSyncMessage(ORDER_TOPIC, ORDER_CREATED_TAG, orderMessage);
            logger.info("订单创建消息发送成功，订单ID: {}, 发送结果: {}", orderMessage.getOrderId(), result);
            return result;
        } catch (Exception e) {
            logger.error("订单创建消息发送失败，订单ID: {}", orderMessage.getOrderId(), e);
            throw new RuntimeException("订单创建消息发送失败", e);
        }
    }

    @Override
    public SendResult sendOrderPaidMessage(OrderMessage orderMessage) {
        logger.info("开始发送订单支付消息，订单ID: {}", orderMessage.getOrderId());
        try {
            SendResult result = rocketMQMessageService.sendSyncMessage(ORDER_TOPIC, ORDER_PAID_TAG, orderMessage);
            logger.info("订单支付消息发送成功，订单ID: {}, 发送结果: {}", orderMessage.getOrderId(), result);
            return result;
        } catch (Exception e) {
            logger.error("订单支付消息发送失败，订单ID: {}", orderMessage.getOrderId(), e);
            throw new RuntimeException("订单支付消息发送失败", e);
        }
    }

    @Override
    public SendResult sendOrderCompletedMessage(OrderMessage orderMessage) {
        logger.info("开始发送订单完成消息，订单ID: {}", orderMessage.getOrderId());
        try {
            SendResult result = rocketMQMessageService.sendSyncMessage(ORDER_TOPIC, ORDER_COMPLETED_TAG, orderMessage);
            logger.info("订单完成消息发送成功，订单ID: {}, 发送结果: {}", orderMessage.getOrderId(), result);
            return result;
        } catch (Exception e) {
            logger.error("订单完成消息发送失败，订单ID: {}", orderMessage.getOrderId(), e);
            throw new RuntimeException("订单完成消息发送失败", e);
        }
    }

    @Override
    public SendResult sendOrderCancelledMessage(OrderMessage orderMessage) {
        logger.info("开始发送订单取消消息，订单ID: {}", orderMessage.getOrderId());
        try {
            SendResult result = rocketMQMessageService.sendSyncMessage(ORDER_TOPIC, ORDER_CANCELLED_TAG, orderMessage);
            logger.info("订单取消消息发送成功，订单ID: {}, 发送结果: {}", orderMessage.getOrderId(), result);
            return result;
        } catch (Exception e) {
            logger.error("订单取消消息发送失败，订单ID: {}", orderMessage.getOrderId(), e);
            throw new RuntimeException("订单取消消息发送失败", e);
        }
    }

    @Override
    public SendResult sendOrderErrorMessage(OrderMessage orderMessage, String errorMsg) {
        logger.info("开始发送订单异常消息，订单ID: {}", orderMessage.getOrderId());
        try {
            // 将错误信息添加到订单消息的备注中
            orderMessage.setRemark(errorMsg);
            SendResult result = rocketMQMessageService.sendSyncMessage(ORDER_TOPIC, ORDER_ERROR_TAG, orderMessage);
            logger.info("订单异常消息发送成功，订单ID: {}, 发送结果: {}", orderMessage.getOrderId(), result);
            return result;
        } catch (Exception e) {
            logger.error("订单异常消息发送失败，订单ID: {}", orderMessage.getOrderId(), e);
            throw new RuntimeException("订单异常消息发送失败", e);
        }
    }
}