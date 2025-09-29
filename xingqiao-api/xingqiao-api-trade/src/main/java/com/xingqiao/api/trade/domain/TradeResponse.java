package com.xingqiao.api.trade.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易响应结果
 * 封装交易操作的返回信息
 * 
 * @author xingqiao
 * @date 2025-09-22
 */
@Data
public class TradeResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易ID
     */
    private Long tradeId;

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
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易时间
     */
    private Date tradeTime;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 错误信息（如果有）
     */
    private String errorMsg;

}