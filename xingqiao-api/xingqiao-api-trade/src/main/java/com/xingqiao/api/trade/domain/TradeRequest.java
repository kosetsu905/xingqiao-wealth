package com.xingqiao.api.trade.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易请求参数
 * 用于创建交易订单的请求对象
 * 
 * @author xingqiao
 * @date 2025-09-22
 */
@Data
public class TradeRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易类型
     * 1: 买入
     * 2: 卖出
     */
    private Integer tradeType;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 交易数量
     */
    private BigDecimal quantity;

    /**
     * 交易价格
     */
    private BigDecimal price;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付方式
     * 1: 余额支付
     * 2: 银行卡支付
     */
    private Integer paymentMethod;

    /**
     * 备注信息
     */
    private String remark;

}