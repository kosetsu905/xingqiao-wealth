package com.xingqiao.order.service.trade.impl;

import com.xingqiao.common.rocketmq.service.RocketMQMessageService;
import com.xingqiao.order.domain.CustomerTradeOrders;
import com.xingqiao.order.service.trade.IOrderMessageService;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单消息服务实现类
 * 实现订单相关的RocketMQ消息发送功能
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Service
public class OrderMessageServiceImpl implements IOrderMessageService {

    private static final Logger log = LoggerFactory.getLogger(OrderMessageServiceImpl.class);

    @Autowired
    private RocketMQMessageService rocketMQMessageService;

    /**
     * 订单创建主题
     */
    private static final String ORDER_CREATE_TOPIC = "ORDER_CREATE_TOPIC";

    /**
     * 订单创建标签
     */
    private static final String ORDER_CREATE_TAG = "ORDER_CREATE_TAG";

    /**
     * 默认账户ID
     */
    private static final Long DEFAULT_ACCOUNT_ID = 16097643L;

    @Override
    public SendResult sendOrderCreateMessage(CustomerTradeOrders order, Long userId, String securityId,
                                           Long direction, BigDecimal price, BigDecimal quantity,
                                           Long orderType, Long accountId) {
        try {
            // 构建订单消息体
            Map<String, Object> orderMsg = new HashMap<>();
            orderMsg.put("orderId", order.getId());
            orderMsg.put("userId", userId);
            orderMsg.put("securityId", securityId);
            orderMsg.put("direction", direction);
            orderMsg.put("price", price);
            orderMsg.put("quantity", quantity);
            orderMsg.put("createTime", new Date());
            orderMsg.put("orderType", orderType);
            orderMsg.put("accountId", accountId != null ? accountId : DEFAULT_ACCOUNT_ID);

            // 发送同步消息
            SendResult result = rocketMQMessageService.sendSyncMessage(ORDER_CREATE_TOPIC, ORDER_CREATE_TAG, orderMsg);
            log.info("已发送RocketMQ订单创建消息，订单ID: {}", order.getId());
            return result;
        } catch (Exception e) {
            log.error("发送订单创建消息失败，订单ID: {}", order.getId(), e);
            throw new RuntimeException("发送订单创建消息失败", e);
        }
    }
}