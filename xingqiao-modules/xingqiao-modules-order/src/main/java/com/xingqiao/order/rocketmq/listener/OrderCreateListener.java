package com.xingqiao.order.rocketmq.listener;

import com.alibaba.fastjson2.JSON;
import com.xingqiao.order.rocketmq.domain.OrderMessage;
import com.xingqiao.order.service.FtTrdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 订单创建消息监听器
 * 用于监听RocketMQ中的订单创建消息，并调用FtTrdService进行实际下单
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "ORDER_CREATE_TOPIC",
        consumerGroup = "ORDER_CREATE_CONSUMER_GROUP",
        selectorExpression = "ORDER_CREATE_TAG",
        consumeThreadMax = 10
)
public class OrderCreateListener implements RocketMQListener<MessageExt> {

    @Autowired
    private FtTrdService ftTrdService;

    @Override
    public void onMessage(MessageExt message) {
        try {
            log.info("收到订单创建消息: msgId={}, topic={}, tags={}", 
                    message.getMsgId(), message.getTopic(), message.getTags());

            // 解析消息内容
            String messageBody = new String(message.getBody(), "UTF-8");
            log.info("消息内容: {}", messageBody);

            // 将JSON字符串转换为Map对象
            Map<String, Object> orderData = JSON.parseObject(messageBody, Map.class);
            
            // 从消息中获取订单相关信息
            String orderId = String.valueOf(orderData.get("orderId"));
            String userId = String.valueOf(orderData.get("userId"));
            String securityId = String.valueOf(orderData.get("securityId"));
            Long direction = ((Number) orderData.get("direction")).longValue();
            Double price = ((Number) orderData.get("price")).doubleValue();
            Long quantity = ((Number) orderData.get("quantity")).longValue();
            
            // 尝试从消息中获取orderType和accountId，如果没有则使用默认值
            Long orderType = 2L; // 默认限价单
            Long accountId = 16097643L; // 默认账户ID
            
            if (orderData.containsKey("orderType")) {
                orderType = ((Number) orderData.get("orderType")).longValue();
            }
            
            if (orderData.containsKey("accountId")) {
                accountId = ((Number) orderData.get("accountId")).longValue();
            }

            log.info("开始处理订单: orderId={}, userId={}, securityId={}, direction={}, price={}, quantity={}, orderType={}, accountId={}",
                    orderId, userId, securityId, direction, price, quantity, orderType, accountId);

            // 调用FtTrdService进行实际下单
            // 这里使用模拟环境(0)，港股市场(1)
            // 注意：实际项目中需要根据业务需求确定trdEnv、trdMarket等参数
            CompletableFuture<String> future = ftTrdService.placeOrder(
                    accountId, // 账户ID
                    0, // 0=模拟环境
                    1, // 1=港股市场
                    direction.intValue(), // 1=买入，2=卖出
                    orderType.intValue(), // 订单类型
                    1, // 1=港股证券市场
                    securityId, // 股票代码
                    price, // 价格
                    quantity // 数量
            );

            // 处理下单结果
            future.thenAccept(result -> {
                log.info("FtTrdService下单成功，订单ID: {}, 结果: {}", orderId, result);
                // 这里可以添加下单成功后的业务逻辑，如更新订单状态等
            }).exceptionally(ex -> {
                log.error("FtTrdService下单失败，订单ID: {}", orderId, ex);
                // 这里可以添加下单失败后的业务逻辑，如重试、记录失败日志等
                return null;
            });

            log.info("订单处理完成: orderId={}", orderId);

        } catch (Exception e) {
            log.error("处理订单创建消息异常", e);
            // 消息消费失败，会触发重试机制
            throw new RuntimeException("处理订单创建消息失败: " + e.getMessage(), e);
        }
    }
}