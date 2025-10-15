package com.xingqiao.order.service;

import org.apache.rocketmq.client.producer.SendResult;
import com.xingqiao.order.domain.CustomerTradeOrders;
import java.math.BigDecimal;

/**
 * 订单消息服务接口
 * 负责发送订单相关的RocketMQ消息
 *
 * @author xingqiao
 * @date 2025-09-22
 */
public interface IOrderMessageService {

    /**
     * 发送订单创建成功消息
     *
     * @param order 订单对象
     * @param userId 用户ID
     * @param securityId 证券ID
     * @param direction 买卖方向
     * @param price 价格
     * @param quantity 数量
     * @param orderType 订单类型
     * @param accountId 账户ID
     * @return 发送结果
     */
    SendResult sendOrderCreateMessage(CustomerTradeOrders order, Long userId, String securityId,
                                      Long direction, BigDecimal price, BigDecimal quantity,
                                      Long orderType, Long accountId);
}