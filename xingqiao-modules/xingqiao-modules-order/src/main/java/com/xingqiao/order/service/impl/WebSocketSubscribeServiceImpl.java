package com.xingqiao.order.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuoteList;
import com.xingqiao.api.trade.domain.SubscribeRequest;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.config.TradeConstants;
import com.xingqiao.order.rocketmq.domain.QuoteMessage;
import com.xingqiao.order.rocketmq.service.QuoteMessageService;
import com.xingqiao.order.service.WebSocketSubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 订阅处理类
 * 订阅信息保存到redis
 * 后续定时任务查询redis订阅信息去处理
 */
@Service
public class WebSocketSubscribeServiceImpl implements WebSocketSubscribeService {
    private static final Logger log = LoggerFactory.getLogger(WebSocketSubscribeServiceImpl.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private QuoteMessageService quoteMessageService;


    /**
     * 订阅股票数据
     *
     * @param queryStockQuoteList
     * @return
     */
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
            String sessionId = redisService.getCacheObject(
                    TradeConstants.USER_SESSION_KEY_PREFIX + queryStockQuoteList.getUserId());
            if (StringUtils.isEmpty(sessionId)) {
                log.error("订阅股票失败：用户{}未建立websocket连接", queryStockQuoteList.getUserId());
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

                    redisService.addCacheSet(TradeConstants.STOCK_INFO_PREFIX + sessionId, stockIdentifier);
                    redisService.expire(TradeConstants.STOCK_INFO_PREFIX + sessionId, TradeConstants.EXPIRE_TIME, TimeUnit.SECONDS);

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
     * 订阅
     *
     * @param request
     */
    @Override
    public void subscribe(SubscribeRequest request) {

        String dataType = request.getDataType();
        switch (dataType) {
            case "global_indices":
                subscribeStock(request);
                break;
            default:
                log.warn("不支持的订阅类型：{}", dataType);
                break;
        }

    }

    private void subscribeStock(SubscribeRequest request) {
        JSONObject msgObj = request.getMsgObj();
        List<QueryStockQuote> list = msgObj.getList("params", QueryStockQuote.class);
        log.info("订阅股票行情订阅开始，批量数量：{}", list.size());
        try {
            // 参数验证
            if (list.isEmpty()) {
                log.warn("订阅股票失败：股票列表为空");
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
                        log.warn("订阅股票失败：股票代码不能为空");
                        continue;
                    }

                    if (queryStockQuote.getMarketCode() == null) {
                        log.warn("订阅股票失败：市场代码不能为空，股票代码：{}", queryStockQuote.getStockCode());
                        continue;
                    }

                    // 获取产品代码，如果为空则默认为"stock"
                    String productCode = queryStockQuote.getProductCode() != null ? queryStockQuote.getProductCode() : "stock";
                    String stockCode = queryStockQuote.getStockCode();
                    String marketCode = queryStockQuote.getMarketCode();

                    // 1. 构建股票唯一标识符：product:market:code
                    String stockIdentifier = productCode + ":" + marketCode + ":" + stockCode;

                    redisService.addCacheSet(TradeConstants.STOCK_INFO_PREFIX + sessionId, stockIdentifier);
                    redisService.expire(TradeConstants.STOCK_INFO_PREFIX + sessionId, TradeConstants.EXPIRE_TIME, TimeUnit.SECONDS);

                    log.info("订阅股票成功：{}", stockIdentifier);
                    successCount++;
                } catch (Exception e) {
                    String stockCode = queryStockQuote != null ? queryStockQuote.getStockCode() : "未知";
                    log.error("订阅股票异常，股票代码：{}", stockCode, e);
                }
            }
            if (successCount == 0) {
                log.error("订阅股票失败：全部订阅失败");
                return;
            }

            //推送mq消息触发订阅股票查询逻辑并通过websocket推送前端
            QuoteMessage quoteMessage = new QuoteMessage();
            quoteMessage.setUserId(request.getUserId());
            quoteMessage.setSessionId(sessionId);
            quoteMessage.setRemark("触发订阅股票查询");
            quoteMessage.setCreateTime(new Date());
            quoteMessageService.sendQuoteQueryMessage(quoteMessage);
        } catch (Exception e) {
            log.error("批量订阅股票异常：", e);
        }
    }

    /**
     * 取消订阅
     *
     * @param request
     */
    @Override
    public void unsubscribe(SubscribeRequest request) {
        String dataType = request.getDataType();
        switch (dataType) {
            case "global_indices":
                unsubscribeStock(request);
                break;
            default:
                log.warn("不支持的取消订阅类型：{}", dataType);
                break;
        }
    }

    private void unsubscribeStock(SubscribeRequest request) {
        JSONObject msgObj = request.getMsgObj();
        List<QueryStockQuote> list = msgObj.getList("params", QueryStockQuote.class);
        log.info("取消订阅股票行情开始，批量数量：{}", list != null ? list.size() : 0);
        try {
            // 参数验证
            if (list.isEmpty()) {
                log.warn("取消订阅股票失败：股票列表为空");
                return;
            }
            // 记录成功取消订阅的数量
            int successCount = 0;
            // 遍历批量取消订阅列表
            for (QueryStockQuote queryStockQuote : list) {
                try {
                    // 单个股票参数验证
                    if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
                        log.warn("取消订阅股票失败：股票代码不能为空");
                        continue;
                    }

                    if (queryStockQuote.getMarketCode() == null) {
                        log.warn("取消订阅股票失败：市场代码不能为空，股票代码：{}", queryStockQuote.getStockCode());
                        continue;
                    }
                    String sessionId=request.getSessionId();
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
                }
            }
            if (successCount == 0) {
               log.error("取消订阅股票失败：全部取消订阅失败");
            }
        } catch (Exception e) {
            log.error("批量取消订阅股票异常：", e);
        }
    }
}
