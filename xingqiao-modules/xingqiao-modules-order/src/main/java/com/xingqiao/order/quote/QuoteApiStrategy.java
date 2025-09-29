package com.xingqiao.order.quote;

import com.xingqiao.api.trade.domain.QueryStockQuoteList;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;

import java.util.List;

/**
 * 行情API策略接口
 * 定义行情相关的操作策略
 * 
 * @author xingqiao
 * @date 2025-09-23
 */
public interface QuoteApiStrategy {
    
    /**
     * 获取单只股票行情
     * @return 股票行情信息
     */
    R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote);
    
    /**
     * 批量获取股票行情
     * @return 股票行情列表
     */
    R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list);
    
    /**
     * 获取历史行情数据
     * @return 历史行情列表
     */
    R<List<StockQuote>> getStockQuoteHistory(QueryStockQuote queryStockQuote);
    
    /**
     * 订阅股票行情
     * @return 订阅结果
     */
    R subscribeStockQuote(QueryStockQuoteList queryStockQuoteList);
    
    /**
     * 取消订阅股票行情
     * @return 取消订阅结果
     */
    R unsubscribeStockQuote(QueryStockQuoteList queryStockQuoteList);
    
    /**
     * 获取策略名称
     * @return 策略名称
     */
    String getStrategyName();
}