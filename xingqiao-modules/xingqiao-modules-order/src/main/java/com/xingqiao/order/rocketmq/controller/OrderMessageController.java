package com.xingqiao.order.rocketmq.controller;

import com.xingqiao.order.rocketmq.domain.OrderMessage;
import com.xingqiao.order.rocketmq.service.OrderMessageService;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * RocketMQ消息控制器
 * 提供API接口来测试消息发送功能
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@RestController
@RequestMapping("/rocketmq/order")
public class OrderMessageController {

    @Autowired
    private OrderMessageService orderMessageService;

    /**
     * 发送订单创建消息
     */
    @PostMapping("/send-created")
    public Result<SendResult> sendOrderCreatedMessage(@RequestBody OrderMessage orderMessage) {
        try {
            // 确保订单ID和创建时间不为空
            if (orderMessage.getOrderId() == null) {
                orderMessage.setOrderId(UUID.randomUUID().toString().replace("-", ""));
            }
            if (orderMessage.getCreateTime() == null) {
                orderMessage.setCreateTime(new Date());
            }
            
            SendResult result = orderMessageService.sendOrderCreatedMessage(orderMessage);
            return Result.success("订单创建消息发送成功", result);
        } catch (Exception e) {
            return Result.fail("订单创建消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 发送订单支付消息
     */
    @PostMapping("/send-paid")
    public Result<SendResult> sendOrderPaidMessage(@RequestBody OrderMessage orderMessage) {
        try {
            // 确保订单ID不为空
            if (orderMessage.getOrderId() == null) {
                orderMessage.setOrderId(UUID.randomUUID().toString().replace("-", ""));
            }
            
            SendResult result = orderMessageService.sendOrderPaidMessage(orderMessage);
            return Result.success("订单支付消息发送成功", result);
        } catch (Exception e) {
            return Result.fail("订单支付消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 发送订单完成消息
     */
    @PostMapping("/send-completed")
    public Result<SendResult> sendOrderCompletedMessage(@RequestBody OrderMessage orderMessage) {
        try {
            // 确保订单ID不为空
            if (orderMessage.getOrderId() == null) {
                orderMessage.setOrderId(UUID.randomUUID().toString().replace("-", ""));
            }
            
            SendResult result = orderMessageService.sendOrderCompletedMessage(orderMessage);
            return Result.success("订单完成消息发送成功", result);
        } catch (Exception e) {
            return Result.fail("订单完成消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 发送订单取消消息
     */
    @PostMapping("/send-cancelled")
    public Result<SendResult> sendOrderCancelledMessage(@RequestBody OrderMessage orderMessage) {
        try {
            // 确保订单ID不为空
            if (orderMessage.getOrderId() == null) {
                orderMessage.setOrderId(UUID.randomUUID().toString().replace("-", ""));
            }
            
            SendResult result = orderMessageService.sendOrderCancelledMessage(orderMessage);
            return Result.success("订单取消消息发送成功", result);
        } catch (Exception e) {
            return Result.fail("订单取消消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 发送订单异常消息
     */
    @PostMapping("/send-error")
    public Result<SendResult> sendOrderErrorMessage(@RequestBody OrderMessage orderMessage, @RequestParam String errorMsg) {
        try {
            // 确保订单ID不为空
            if (orderMessage.getOrderId() == null) {
                orderMessage.setOrderId(UUID.randomUUID().toString().replace("-", ""));
            }
            
            SendResult result = orderMessageService.sendOrderErrorMessage(orderMessage, errorMsg);
            return Result.success("订单异常消息发送成功", result);
        } catch (Exception e) {
            return Result.fail("订单异常消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 生成测试订单消息
     * 用于快速测试消息发送功能
     */
    @GetMapping("/generate-test-order")
    public Result<OrderMessage> generateTestOrderMessage() {
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId("TEST_ORDER_" + UUID.randomUUID().toString().substring(0, 8));
        orderMessage.setCustomerId("CUSTOMER_" + (int)(Math.random() * 10000));
        orderMessage.setOrderType("BUY");
        orderMessage.setAmount(new BigDecimal("1000.00"));
        orderMessage.setStatus("CREATED");
        orderMessage.setCreateTime(new Date());
        orderMessage.setRemark("测试订单消息");
        
        return Result.success("测试订单消息生成成功", orderMessage);
    }

    /**
     * 统一响应结果封装类
     */
    public static class Result<T> {
        private int code;
        private String message;
        private T data;

        private Result(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public static <T> Result<T> success(String message, T data) {
            return new Result<>(200, message, data);
        }

        public static <T> Result<T> fail(String message) {
            return new Result<>(500, message, null);
        }

        // Getters and Setters
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}