package com.xingqiao.order.service;

import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;

public interface TradeApiService {

    /**
     * 创建交易订单
     * @param userId 当前登录用户ID
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    R createTrade(Long userId, TradeRequest tradeRequest);

}
