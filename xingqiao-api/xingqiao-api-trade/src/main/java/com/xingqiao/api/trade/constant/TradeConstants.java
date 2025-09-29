package com.xingqiao.api.trade.constant;

/**
 * 交易服务常量类
 * 定义交易相关的常量值
 * 
 * @author xingqiao
 * @date 2025-09-22
 */
public interface TradeConstants {
    
    /**
     * 交易类型 - 买入
     */
    Integer TRADE_TYPE_BUY = 1;
    
    /**
     * 交易类型 - 卖出
     */
    Integer TRADE_TYPE_SELL = 2;
    
    /**
     * 交易状态 - 待处理
     */
    Integer TRADE_STATUS_PENDING = 0;
    
    /**
     * 交易状态 - 处理中
     */
    Integer TRADE_STATUS_PROCESSING = 1;
    
    /**
     * 交易状态 - 交易成功
     */
    Integer TRADE_STATUS_SUCCESS = 2;
    
    /**
     * 交易状态 - 交易失败
     */
    Integer TRADE_STATUS_FAILED = 3;
    
    /**
     * 交易状态 - 已取消
     */
    Integer TRADE_STATUS_CANCELLED = 4;
    
    /**
     * 支付方式 - 余额支付
     */
    Integer PAYMENT_METHOD_BALANCE = 1;
    
    /**
     * 支付方式 - 银行卡支付
     */
    Integer PAYMENT_METHOD_BANK_CARD = 2;

    /**
     * 服务名称
     */
    String SERVICE_NAME = "xingqiao-modules-trade";
    
}