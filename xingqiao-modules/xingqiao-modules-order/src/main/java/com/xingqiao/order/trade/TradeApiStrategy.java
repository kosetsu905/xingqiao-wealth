package com.xingqiao.order.trade;

import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;

/**
 * 交易API策略接口
 * 定义交易相关的操作策略
 * 
 * @author xingqiao
 * @date 2025-09-23
 */
public interface TradeApiStrategy {
    
    /**
     * 获取交易详情
     * @param tradeId 交易ID
     * @return 交易详情
     */
    R getTradeDetail(Long tradeId);
    
    /**
     * 创建交易订单
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    R createTrade(TradeRequest tradeRequest);

    
    /**
     * 获取策略名称
     * @return 策略名称
     */
    String getStrategyName();
}