package com.xingqiao.api.trade.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
     * 资金账户ID
     * 数据库字段类型: BIGINT UNSIGNED
     */
    private Long accountId;

    /**
     * 证券ID
     * 数据库字段类型: BIGINT UNSIGNED
     */
    private Long securityId;

    /**
     * 订单类型: 1-限价单, 2-市价单, 3-条件单
     * 数据库字段类型: TINYINT
     */
    private Integer orderType = 1; // 默认限价单

    /**
     * 买卖方向: 1-买入, 2-卖出
     * 数据库字段类型: TINYINT
     */
    private Integer direction;

    /**
     * 委托价格(市价单可为0)
     * 数据库字段类型: DECIMAL(18,6)
     */
    private BigDecimal price = BigDecimal.ZERO;

    /**
     * 委托数量
     * 数据库字段类型: DECIMAL(18,6)
     */
    private BigDecimal quantity = BigDecimal.ZERO;

    /**
     * 订单过期时间
     * 数据库字段类型: DATETIME(3)
     */
    private Date expireTime = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 默认过期时间：当前时间加一天

    /**
     * 条件单类型: 1-价格条件, 2-时间条件（条件单时使用）
     * 数据库字段类型: TINYINT
     */
    private Integer conditionType;

    /**
     * 条件值（条件单时使用）
     * 数据库字段类型: DECIMAL(18,6)
     */
    private BigDecimal conditionValue = BigDecimal.ZERO;

    /**
     * 备注信息
     * 数据库字段类型: VARCHAR(500)
     */
    private String remark = "";
}