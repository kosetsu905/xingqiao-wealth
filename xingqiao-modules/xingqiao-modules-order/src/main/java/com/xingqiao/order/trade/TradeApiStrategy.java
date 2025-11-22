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
     * 创建交易订单
     * @param userId 当前登录用户ID
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    R createTrade(Long userId, TradeRequest tradeRequest);
}