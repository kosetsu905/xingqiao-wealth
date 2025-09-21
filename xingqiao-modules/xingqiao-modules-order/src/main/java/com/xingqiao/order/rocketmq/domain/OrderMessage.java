package com.xingqiao.order.rocketmq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单消息实体类
 * 用于在RocketMQ中传递订单信息
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 订单类型（如：买入、卖出）
     */
    private String orderType;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注信息
     */
    private String remark;
}