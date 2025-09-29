package com.xingqiao.order.quote.impl;

import com.xingqiao.api.trade.domain.QueryStockQuoteList;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.config.TradeConstants;
import com.xingqiao.order.quote.QuoteApiStrategy;
import com.xingqiao.order.rocketmq.domain.QuoteMessage;
import com.xingqiao.order.rocketmq.service.QuoteMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private QuoteMessageService quoteMessageService;


    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        return R.fail("默认策略不支持获取单只股票行情");
    }

    @Override
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list) {
        return R.fail("默认策略不支持获取单只股票行情");
    }

    @Override
    public R<List<StockQuote>> getStockQuoteHistory(QueryStockQuote queryStockQuote) {
        return R.fail("默认策略不支持获取单只股票行情");
    }

    @Override
    public R subscribeStockQuote(QueryStockQuoteList queryStockQuoteList) {
        log.info("订阅股票行情订阅开始，批量数量：{}", queryStockQuoteList != null ? queryStockQuoteList.getList().size() : 0);
        try {
            // 参数验证
            if (queryStockQuoteList == null || queryStockQuoteList.getList() == null
                    || queryStockQuoteList.getList().isEmpty()) {
                log.warn("订阅股票失败：股票列表为空");
                return R.fail("股票列表不能为空");
            }
            //根据userId查看用户是否websocket建立连接
            String sessionId = redisService.getCacheObject(TradeConstants.USER_SESSION_KEY_PREFIX + queryStockQuoteList.getUserId());
            if (StringUtils.isEmpty(sessionId)) {
                log.warn("订阅股票失败：用户未建立websocket连接");
                return R.fail("用户未建立websocket连接");
            }

            queryStockQuoteList.setSessionId(sessionId);
            // 记录成功订阅的数量
            int successCount = 0;
            List<String> failedStocks = new ArrayList<>();
            // 遍历批量订阅列表
            for (QueryStockQuote queryStockQuote : queryStockQuoteList.getList()) {
                try {
                    // 单个股票参数验证
                    if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
                        log.warn("订阅股票失败：股票代码不能为空");
                        failedStocks.add("空股票代码");
                        continue;
                    }

                    if (queryStockQuote.getMarketCode() == null) {
                        log.warn("订阅股票失败：市场代码不能为空，股票代码：{}", queryStockQuote.getStockCode());
                        failedStocks.add(queryStockQuote.getStockCode() + "(市场代码为空)");
                        continue;
                    }

                    // 获取产品代码，如果为空则默认为"stock"
                    String productCode = queryStockQuote.getProductCode() != null ? queryStockQuote.getProductCode() : "stock";
                    String stockCode = queryStockQuote.getStockCode();
                    String marketCode = queryStockQuote.getMarketCode();

                    // 1. 构建股票唯一标识符：product:market:code
                    String stockIdentifier = productCode + ":" + marketCode + ":" + stockCode;

                    redisService.addCacheSet(TradeConstants.STOCK_INFO_PREFIX  + sessionId, stockIdentifier);
                    redisService.expire(TradeConstants.STOCK_INFO_PREFIX  + sessionId, TradeConstants.EXPIRE_TIME, TimeUnit.SECONDS);

                    log.info("订阅股票成功：{}", stockIdentifier);
                    successCount++;
                } catch (Exception e) {
                    String stockCode = queryStockQuote != null ? queryStockQuote.getStockCode() : "未知";
                    log.error("订阅股票异常，股票代码：{}", stockCode, e);
                    failedStocks.add(stockCode);
                }
            }

            if (failedStocks.isEmpty()) {
                //推送mq消息触发订阅股票查询逻辑并通过websocket推送前端
                QuoteMessage quoteMessage = new QuoteMessage();
                quoteMessage.setUserId(queryStockQuoteList.getUserId());
                quoteMessage.setSessionId(sessionId);
                quoteMessage.setRemark("触发订阅股票查询");
                quoteMessage.setCreateTime(new Date());
                quoteMessageService.sendQuoteQueryMessage(quoteMessage);
                return R.ok("全部订阅成功");
            } else if (successCount == 0) {
                return R.fail("全部订阅失败");
            } else {
                //推送mq消息触发订阅股票查询逻辑并通过websocket推送前端
                QuoteMessage quoteMessage = new QuoteMessage();
                quoteMessage.setUserId(queryStockQuoteList.getUserId());
                quoteMessage.setSessionId(sessionId);
                quoteMessage.setRemark("触发订阅股票查询");
                quoteMessage.setCreateTime(new Date());
                quoteMessageService.sendQuoteQueryMessage(quoteMessage);
                return R.ok("部分订阅成功");
            }
        } catch (Exception e) {
            log.error("批量订阅股票异常：", e);
            return R.fail("订阅失败：" + e.getMessage());
        }
    }

    @Override
    public R unsubscribeStockQuote(QueryStockQuoteList queryStockQuoteList) {
        log.info("取消订阅股票行情开始，批量数量：{}", queryStockQuoteList != null ? queryStockQuoteList.getList().size() : 0);

        try {
            // 参数验证
            if (queryStockQuoteList == null || queryStockQuoteList.getList() == null
                    || queryStockQuoteList.getList().isEmpty()) {
                log.warn("取消订阅股票失败：股票列表为空");
                return R.fail("股票列表不能为空");
            }

            //根据userId查看用户是否websocket建立连接
            String sessionId = redisService.getCacheObject(TradeConstants.USER_SESSION_KEY_PREFIX + queryStockQuoteList.getUserId());
            if (StringUtils.isEmpty(sessionId)) {
                log.warn("取消订阅股票失败：用户未建立websocket连接");
                return R.fail("用户未建立websocket连接");
            }
            queryStockQuoteList.setSessionId(sessionId);
            // 记录成功取消订阅的数量
            int successCount = 0;
            List<String> failedStocks = new ArrayList<>();

            // 遍历批量取消订阅列表
            for (QueryStockQuote queryStockQuote : queryStockQuoteList.getList()) {
                try {
                    // 单个股票参数验证
                    if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
                        log.warn("取消订阅股票失败：股票代码不能为空");
                        failedStocks.add("空股票代码");
                        continue;
                    }

                    if (queryStockQuote.getMarketCode() == null) {
                        log.warn("取消订阅股票失败：市场代码不能为空，股票代码：{}", queryStockQuote.getStockCode());
                        failedStocks.add(queryStockQuote.getStockCode() + "(市场代码为空)");
                        continue;
                    }

                    // 获取产品代码，如果为空则默认为"stock"
                    String productCode = queryStockQuote.getProductCode() != null ? queryStockQuote.getProductCode() : "stock";
                    String stockCode = queryStockQuote.getStockCode();
                    String marketCode = queryStockQuote.getMarketCode();
                    // 1. 构建股票唯一标识符：product:market:code
                    String stockIdentifier = productCode + ":" + marketCode + ":" + stockCode;
                    redisService.removeCacheSet(TradeConstants.STOCK_INFO_PREFIX + sessionId, stockIdentifier);
                    log.info("取消订阅股票成功：{}", stockIdentifier);
                    successCount++;
                } catch (Exception e) {
                    String stockCode = queryStockQuote != null ? queryStockQuote.getStockCode() : "未知";
                    log.error("取消订阅股票异常，股票代码：{}", stockCode, e);
                    failedStocks.add(stockCode);
                }
            }

            if (failedStocks.isEmpty()) {
                return R.ok("全部取消订阅成功");
            } else if (successCount == 0) {
                return R.fail("全部取消订阅失败");
            } else {
                return R.ok("部分取消订阅成功");
            }
        } catch (Exception e) {
            log.error("批量取消订阅股票异常：", e);
            return R.fail("取消订阅失败：" + e.getMessage());
        }
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