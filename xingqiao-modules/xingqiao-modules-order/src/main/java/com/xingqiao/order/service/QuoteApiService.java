package com.xingqiao.order.service;

import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;

import java.util.List;

public interface QuoteApiService {


    /**
     * 获取 单只实时股票行情
     * 不保存缓存
     * @return 股票行情信息
     */
    R<StockQuote> getStockCurrentQuote(QueryStockQuote queryStockQuote);


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
     */
    R<List<StockQuote>> getStockQuoteChartList(List<QueryStockQuote> list);


}
