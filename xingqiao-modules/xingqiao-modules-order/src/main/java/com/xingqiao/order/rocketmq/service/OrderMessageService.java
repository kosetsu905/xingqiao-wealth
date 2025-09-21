package com.xingqiao.order.rocketmq.service;

import com.xingqiao.order.rocketmq.domain.OrderMessage;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * 订单消息服务接口
 * 定义订单消息发送的核心方法
 *
 * @author xingqiao
 * @date 2025-09-22
 */
public interface OrderMessageService {

    /**
     * 发送订单创建消息
     *
     * @param orderMessage 订单消息
     * @return 发送结果
     */
    SendResult sendOrderCreatedMessage(OrderMessage orderMessage);

    /**
     * 发送订单支付消息
     *
     * @param orderMessage 订单消息
     * @return 发送结果
     */
    SendResult sendOrderPaidMessage(OrderMessage orderMessage);

    /**
     * 发送订单完成消息
     *
     * @param orderMessage 订单消息
     * @return 发送结果
     */
    SendResult sendOrderCompletedMessage(OrderMessage orderMessage);

    /**
     * 发送订单取消消息
     *
     * @param orderMessage 订单消息
     * @return 发送结果
     */
    SendResult sendOrderCancelledMessage(OrderMessage orderMessage);

    /**
     * 发送订单异常消息
     *
     * @param orderMessage 订单消息
     * @param errorMsg 错误信息
     * @return 发送结果
     */
    SendResult sendOrderErrorMessage(OrderMessage orderMessage, String errorMsg);
}