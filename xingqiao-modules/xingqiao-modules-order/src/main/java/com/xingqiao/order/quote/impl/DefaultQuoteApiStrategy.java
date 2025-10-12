package com.xingqiao.order.quote.impl;

import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.quote.QuoteApiStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.xingqiao.order.config.TradeConstants.getRedisKey;

/**
 * 默认行情API策略实现类
 * 实现QuoteApiStrategy接口，提供基本的行情功能实现
 *
 * @author xingqiao
 * @date 2025-09-23
 */
@Component
public class DefaultQuoteApiStrategy implements QuoteApiStrategy {

    private static final Logger log = LoggerFactory.getLogger(DefaultQuoteApiStrategy.class);


    @Autowired
    private RedisService redisService;



    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        return R.fail("无需实现");
    }

    @Override
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list) {
        log.info("批量获取股票行情，数量：{}", list != null ? list.size() : 0);
        List<StockQuote> resultList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {
            for (QueryStockQuote queryStockQuote : list) {
                if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
                    continue;
                }
                // 构建缓存键
                String redisKey = getRedisKey(queryStockQuote);
                // 尝试从缓存获取
                StockQuote cachedQuote = redisService.getCacheObject(redisKey);
                if (!Objects.isNull(cachedQuote)) {
                    resultList.add(cachedQuote);
                }
            }
        }
        log.info("批量获取股票行情，数量：{}，缓存命中数量：{}", list==null? 0:list.size(), resultList.size());
        return R.ok(resultList);
    }

    @Override
    public R<List<StockQuote>> getStockQuoteHistory(QueryStockQuote queryStockQuote) {
        return R.fail("默认策略不支持获取单只股票行情");
    }

    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    @Override
    public String getStrategyName() {
        return "default";
    }
}
