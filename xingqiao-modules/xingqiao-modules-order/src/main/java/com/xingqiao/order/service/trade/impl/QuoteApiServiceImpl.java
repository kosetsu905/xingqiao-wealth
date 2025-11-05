package com.xingqiao.order.service.trade.impl;

import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.order.config.StockQueryMappingConfig;
import com.xingqiao.order.service.QuoteApiService;
import com.xingqiao.order.quote.QuoteApiStrategy;
import com.xingqiao.order.quote.QuoteApiStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("quoteApiService")
public class QuoteApiServiceImpl  implements QuoteApiService {
    private static final Logger log = LoggerFactory.getLogger(QuoteApiServiceImpl.class);

    @Autowired
    private QuoteApiStrategyFactory quoteApiStrategyFactory;

    @Autowired
    private StockQueryMappingConfig stockQueryMappingConfig;


    /**
     * 获取单只股票行情
     * @return 股票行情信息
     */
    @Override
    public R<StockQuote> getStockCurrentQuote(QueryStockQuote queryStockQuote) {
        try {
            String strategyName=stockQueryMappingConfig.getStockTradeStrategy();
            // 通过工厂获取指定策略
            QuoteApiStrategy strategy = quoteApiStrategyFactory.getStrategy(strategyName);
            if (strategy == null) {
                return R.fail("未找到指定的策略: " + strategyName);
            }
            // 直接调用策略的方法
            return strategy.getStockCurrentQuote(queryStockQuote);
        } catch (Exception e) {
            log.error("获取股票行情失败"+e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 获取单只股票行情
     * @return 股票行情信息
     */
    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        try {
            String strategyName=stockQueryMappingConfig.getStockTradeStrategy();
            // 通过工厂获取指定策略
            QuoteApiStrategy strategy = quoteApiStrategyFactory.getStrategy(strategyName);
            if (strategy == null) {
                return R.fail("未找到指定的策略: " + strategyName);
            }
            // 直接调用策略的方法
            return strategy.getStockQuote(queryStockQuote);
        } catch (Exception e) {
            return R.fail("获取股票行情失败：" + e.getMessage());
        }
    }

    /**
     * 批量获取股票行情
     * @return 股票行情列表
     */
    @Override
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list) {
        try {
            String strategyName=stockQueryMappingConfig.getStockTradeStrategy();
            // 通过工厂获取指定策略
            QuoteApiStrategy strategy = quoteApiStrategyFactory.getStrategy(strategyName);
            if (strategy == null) {
                return R.fail("未找到指定的策略: " + strategyName);
            }
            // 直接调用策略的方法
            return strategy.getStockQuoteList(list);
        } catch (Exception e) {
            return R.fail("获取股票行情失败：" + e.getMessage());
        }
    }

    /**
     * 获取区间行情数据
     */
    @Override
    public R<List<StockQuote>> getStockQuoteChartList(List<QueryStockQuote> list) {
        try {
            String strategyName=stockQueryMappingConfig.getStockTradeStrategy();
            // 通过工厂获取指定策略
            QuoteApiStrategy strategy = quoteApiStrategyFactory.getStrategy(strategyName);
            if (strategy == null) {
                return R.fail("未找到指定的策略: " + strategyName);
            }
            // 直接调用策略的方法
            return strategy.getStockQuoteChartList(list);
        } catch (Exception e) {
            return R.fail("获取股票行情失败：" + e.getMessage());
        }
    }
}
