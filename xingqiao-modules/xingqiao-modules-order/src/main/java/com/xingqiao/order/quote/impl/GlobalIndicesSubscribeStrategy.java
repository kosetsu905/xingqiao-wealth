package com.xingqiao.order.quote.impl;

import com.alibaba.fastjson2.JSONObject;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.SubscribeRequest;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.config.TradeConstants;
import com.xingqiao.order.quote.QuoteApiStrategyFactory;
import com.xingqiao.order.quote.SubscribeStrategy;
import com.xingqiao.order.rocketmq.domain.QuoteMessage;
import com.xingqiao.order.service.WebSocketService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * 首页全球指数订阅
 */
@Service
public class GlobalIndicesSubscribeStrategy implements SubscribeStrategy {
    private static final Logger log = LoggerFactory.getLogger(GlobalIndicesSubscribeStrategy.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private QuoteApiStrategyFactory quoteApiStrategyFactory;

    private static final String TAG = "首页全球指数";

    @Override
    public void subscribe(SubscribeRequest request) {
        JSONObject msgObj = request.getMsgObj();
        List<QueryStockQuote> list = msgObj.getList("params", QueryStockQuote.class);
        log.info("{}，订阅开始，批量数量：{}", TAG, list.size());
        try {
            // 参数验证
            if (list.isEmpty()) {
                log.warn("{}订阅股票失败：股票列表为空", TAG);
                return;
            }
            // 记录成功订阅的数量
            int successCount = 0;
            // 遍历批量订阅列表
            String sessionId = request.getSessionId();
            for (QueryStockQuote queryStockQuote : list) {
                try {
                    // 单个股票参数验证
                    if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
                        log.warn("{}订阅失败：股票代码不能为空", TAG);
                        continue;
                    }
                    if (queryStockQuote.getMarketCode() == null) {
                        log.warn("{}订阅失败：市场代码不能为空，股票代码：{}", TAG, queryStockQuote.getStockCode());
                        continue;
                    }

                    // 获取产品代码，如果为空则默认为"stock"
                    String productCode = queryStockQuote.getProductCode() != null ? queryStockQuote.getProductCode() : "stock";
                    String stockCode = queryStockQuote.getStockCode();
                    String marketCode = queryStockQuote.getMarketCode();

                    // 1. 构建股票唯一标识符：product:market:code
                    String stockIdentifier = productCode + ":" + marketCode + ":" + stockCode;

                    redisService.addCacheSet(TradeConstants.STOCK_GLOBAL_INDICES_INFO_PREFIX + sessionId, stockIdentifier);
                    redisService.expire(TradeConstants.STOCK_GLOBAL_INDICES_INFO_PREFIX + sessionId, TradeConstants.EXPIRE_TIME, TimeUnit.SECONDS);

                    log.info("{}订阅成功：{}", TAG, stockIdentifier);
                    successCount++;
                } catch (Exception e) {
                    String stockCode = queryStockQuote != null ? queryStockQuote.getStockCode() : "未知";
                    log.error("{}订阅异常，股票代码：{}", TAG, stockCode, e);
                }
            }
            if (successCount == 0) {
                log.error("{}订阅失败：全部订阅失败", TAG);
                return;
            }

            //查询全球指数行情并推送前端
            QuoteMessage quoteMessage = new QuoteMessage();
            quoteMessage.setUserId(request.getUserId());
            quoteMessage.setSessionId(sessionId);
            quoteMessage.setRemark("触发全球指数行情查询");
            quoteMessage.setCreateTime(new Date());
            processQuoteQuery(quoteMessage);

            // 发送订阅成功消息
            JSONObject response = new JSONObject();
            response.put("type", msgObj.getString("dataType"));
            response.put("action", "subscribe_success");
            response.put("message", "订阅股票行情成功");
            webSocketService.sendMessageToUser(request.getUserId(), response.toJSONString());
            log.info("用户 {} 订阅成功", request.getUserId());


        } catch (Exception e) {
            log.error("{}批量订阅股票异常：", TAG, e);
        }
    }

    @Override
    public void unsubscribe(SubscribeRequest request) {
        JSONObject msgObj = request.getMsgObj();
        List<QueryStockQuote> list = msgObj.getList("params", QueryStockQuote.class);
        log.info("取消首页全球指数行情开始，批量数量：{}", list != null ? list.size() : 0);
        try {
            // 参数验证
            if (list != null && list.isEmpty()) {
                log.warn("取消首页全球指数行情失败：股票列表为空");
                return;
            }
            // 记录成功取消订阅的数量
            int successCount = 0;
            // 遍历批量取消订阅列表
            if (list != null) {
                for (QueryStockQuote queryStockQuote : list) {
                    try {
                        // 单个股票参数验证
                        if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
                            log.warn("取消首页全球指数行情失败：股票代码不能为空");
                            continue;
                        }

                        if (queryStockQuote.getMarketCode() == null) {
                            log.warn("取消首页全球指数行情失败：市场代码不能为空，股票代码：{}", queryStockQuote.getStockCode());
                            continue;
                        }
                        String sessionId = request.getSessionId();
                        // 获取产品代码，如果为空则默认为"stock"
                        String productCode = queryStockQuote.getProductCode() != null ? queryStockQuote.getProductCode() : "stock";
                        String stockCode = queryStockQuote.getStockCode();
                        String marketCode = queryStockQuote.getMarketCode();
                        // 1. 构建股票唯一标识符：product:market:code
                        String stockIdentifier = productCode + ":" + marketCode + ":" + stockCode;
                        redisService.removeCacheSet(TradeConstants.STOCK_GLOBAL_INDICES_INFO_PREFIX + sessionId, stockIdentifier);
                        log.info("取消首页全球指数行情成功：{}", stockIdentifier);
                        successCount++;
                    } catch (Exception e) {
                        String stockCode = queryStockQuote != null ? queryStockQuote.getStockCode() : "未知";
                        log.error("取消首页全球指数行情异常，股票代码：{}", stockCode, e);
                    }
                }
            }
            if (successCount == 0) {
                log.error("取消首页全球指数行情失败：全部取消订阅失败");
            } else {
                // 发送取消订阅成功消息
                JSONObject response = new JSONObject();
                response.put("type", msgObj.getString("dataType"));
                response.put("action", "unsubscribe_success");
                response.put("message", "取消订阅股票行情成功");
                webSocketService.sendMessageToUser(request.getUserId(), response.toJSONString());
                log.info("用户 {} 取消订阅成功", request.getUserId());
            }
        } catch (Exception e) {
            log.error("取消首页全球指数行情异常：", e);
        }
    }

    @Override
    public void processQuoteQuery(QuoteMessage quoteMessage) {
        try {
            // 1. 获取所有订阅的股票信息
            List<QueryStockQuote> subscribedStocks = getAllSubscribedStocks(quoteMessage);
            if (subscribedStocks.isEmpty()) {
                log.warn("当前没有订阅的股票");
                return;
            }
            // 2. 获取行情数据
            R<List<StockQuote>> result = quoteApiStrategyFactory.getStockQuoteList(subscribedStocks);
            if (R.isSuccess(result) && result.getData() != null) {
                // 3. 推送行情数据给前端
                pushStockQuotesToFrontend(quoteMessage, result.getData());
            }
            log.info("发现 {} 只订阅的股票需要查询行情", subscribedStocks.size());

        } catch (Exception e) {
            log.error("处理股票行情查询请求异常", e);
        }
    }


    /**
     * 获取所有订阅的股票信息
     */
    private List<QueryStockQuote> getAllSubscribedStocks(QuoteMessage quoteMessage) {
        List<QueryStockQuote> subscribedStocks = new ArrayList<>();
        try {
            Set<String> subscribedStocksSet = redisService.getCacheSet(TradeConstants.STOCK_GLOBAL_INDICES_INFO_PREFIX + quoteMessage.getSessionId());
            if (CollectionUtils.isNotEmpty(subscribedStocksSet)) {
                for (String stockInfo : subscribedStocksSet) {
                    String[] stockInfoArr = stockInfo.split(":");
                    QueryStockQuote queryStockQuote = new QueryStockQuote();
                    queryStockQuote.setProductCode(stockInfoArr[0]);
                    queryStockQuote.setMarketCode(stockInfoArr[1]);
                    queryStockQuote.setStockCode(stockInfoArr[2]);
                    subscribedStocks.add(queryStockQuote);
                }
            }
        } catch (Exception e) {
            log.error("获取订阅股票列表异常", e);
        }
        return subscribedStocks;
    }


    /**
     * 通过WebSocket推送给前端
     */
    private void pushStockQuotesToFrontend(QuoteMessage quoteMessage, List<StockQuote> stockQuotes) {
        if (stockQuotes == null || stockQuotes.isEmpty()) {
            return;
        }
        try {
            JSONObject stockQuotesJson = new JSONObject();
            stockQuotesJson.put("stockQuotes", stockQuotes);
            stockQuotesJson.put("type", "global_indices");
            webSocketService.sendMessageBySessionId(quoteMessage.getSessionId(), stockQuotesJson.toJSONString());
            log.info("成功推送股票行情数据到前端");
        } catch (Exception e) {
            log.error("推送股票行情数据到前端异常", e);
        }
    }

    @Override
    public String getStrategyName() {
        return "global_indices";
    }
}
