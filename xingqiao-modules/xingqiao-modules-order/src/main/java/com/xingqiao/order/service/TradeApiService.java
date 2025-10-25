package com.xingqiao.order.service;

import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TradeApiService {

    /**
     * 查询交易详情
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
     * 取消交易订单
     * @param tradeId 交易ID
     * @return 取消结果
     */
    R cancelTrade(Long tradeId);

}
