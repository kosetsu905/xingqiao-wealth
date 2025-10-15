package com.xingqiao.order.quote.impl;

import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.service.PolygonService;
import com.xingqiao.order.quote.QuoteApiStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class PolygonQuoteApiStrategy implements QuoteApiStrategy {

    private final PolygonService polygonService;
    private final RedisService redisService;

    public PolygonQuoteApiStrategy(PolygonService polygonService, RedisService redisService) {
        this.polygonService = polygonService;
        this.redisService = redisService;
    }

    /**
     * 获取单只实时股票市场行情
     *
     * @param queryStockQuote
     * @return
     */
    @Override
    public R<StockQuote> getStockCurrentQuote(QueryStockQuote queryStockQuote) {
        try {
            String stockCode = queryStockQuote.getStockCode();
            String redisKey = "polygon:quote:" + stockCode;
            Map<String, Object> data = polygonService.getMarketData(stockCode);
            StockQuote quote = mapToStockQuote(queryStockQuote, data);
            // 缓存 2 分钟
            redisService.setCacheObject(redisKey, quote);
            return R.ok(quote);
        } catch (Exception e) {
            return R.fail("获取市场行情失败: " + e.getMessage());
        }
    }

    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        String stockCode = queryStockQuote.getStockCode();
        String redisKey = "polygon:quote:" + stockCode;

        // 尝试从缓存获取
        StockQuote cachedQuote = redisService.getCacheObject(redisKey);
        if (cachedQuote != null) {
            return R.ok(cachedQuote);
        }

        try {
            Map<String, Object> data = polygonService.getMarketData(stockCode);
            StockQuote quote = mapToStockQuote(queryStockQuote, data);
            // 缓存 2 分钟
            redisService.setCacheObject(redisKey, quote);
            return R.ok(quote);
        } catch (Exception e) {
            return R.fail("获取市场行情失败: " + e.getMessage());
        }
    }

    @Override
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list) {
        List<StockQuote> result = new ArrayList<>();
        for (QueryStockQuote query : list) {
            StockQuote quote = getStockQuote(query).getData();
            if (quote != null) {
                result.add(quote);
            }
        }
        return R.ok(result);
    }

    // 必须实现抽象方法，暂时返回空列表
    @Override
    public R<List<StockQuote>> getStockQuoteChartList(List<QueryStockQuote> list) {
        return R.ok(Collections.emptyList());
    }

    @Override
    public String getStrategyName() {
        return "polygon";
    }

    private StockQuote mapToStockQuote(QueryStockQuote query, Map<String, Object> data) {
        StockQuote quote = new StockQuote();
        quote.setStockCode(query.getStockCode());
        quote.setMarketCode(query.getMarketCode());
        quote.setStockName(query.getStockCode());
        quote.setCurrentPrice(new BigDecimal(data.get("currentPrice").toString().replace(",", "")));
        quote.setPriceChange(new BigDecimal(data.get("priceChange").toString().replace(",", "")));
        quote.setPriceChangePercent(new BigDecimal(data.get("priceChangePercent").toString().replace("%", "")));
        quote.setStatus(0);
        quote.setDataSource("Polygon");
        return quote;
    }
}
