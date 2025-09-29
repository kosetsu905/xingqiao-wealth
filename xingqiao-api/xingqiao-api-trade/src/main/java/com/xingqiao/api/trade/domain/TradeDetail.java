package com.xingqiao.api.trade.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易详情信息
 * 包含交易的完整详细信息
 * 
 * @author xingqiao
 * @date 2025-09-22
 */
@Data
public class TradeDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易ID
     */
    private Long tradeId;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 交易类型
     * 1: 买入
     * 2: 卖出
     */
    private Integer tradeType;

    /**
     * 交易类型名称
     */
    private String tradeTypeName;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 交易数量
     */
    private BigDecimal quantity;

    /**
     * 交易价格
     */
    private BigDecimal price;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 支付方式
     * 1: 余额支付
     * 2: 银行卡支付
     */
    private Integer paymentMethod;

    /**
     * 支付方式名称
     */
    private String paymentMethodName;

    /**
     * 交易状态
     * 0: 待处理
     * 1: 处理中
     * 2: 交易成功
     * 3: 交易失败
     * 4: 已取消
     */
    private Integer status;

    /**
     * 交易状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 交易时间
     */
    private Date tradeTime;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 交易流水号
     */
    private String transactionNo;

}