
package com.xingqiao.order.quote.impl;

import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.quote.QuoteApiStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * itick行情API策略实现类
 * 实现QuoteApiStrategy接口，提供基本的行情功能实现
 * <a href="https://docs.itick.org/rest-api/indices/indices-quotess">...</a>
 * @author xingqiao
 * &#064;date  2025-09-23
 */
@Component
public class ItickQuoteApiStrategy implements QuoteApiStrategy {

    private static final Logger log = LoggerFactory.getLogger(ItickQuoteApiStrategy.class);

    // Redis键前缀
    private static final String STOCK_SUBSCRIPTION_PREFIX = "stock:subscription:";
    private static final String MARKET_STOCKS_PREFIX = "market:stocks:";

    private final Random random = new Random();

    @Autowired
    private RedisService redisService;


    @Override
    public R<StockQuote> getStockCurrentQuote(QueryStockQuote queryStockQuote) {
        return null;
    }

    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        return R.fail("不支持获取单只股票行情");
    }

    @Override
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list) {
        return R.fail("不支持获取单只股票行情");
    }

    @Override
    public R<List<StockQuote>> getStockQuoteChartList(List<QueryStockQuote> list) {
        return null;
    }

    /**
     * 获取策略名称
     * @return 策略名称
     */
    @Override
    public String getStrategyName() {
        return "itick";
    }
}
