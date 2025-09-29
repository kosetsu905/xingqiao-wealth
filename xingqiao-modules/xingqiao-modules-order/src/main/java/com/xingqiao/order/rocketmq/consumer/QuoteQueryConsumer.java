package com.xingqiao.order.rocketmq.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.common.rocketmq.consumer.RocketMQConsumerHelper;
import com.xingqiao.order.config.TradeConstants;
import com.xingqiao.order.quote.QuoteApiStrategyFactory;
import com.xingqiao.order.rocketmq.domain.QuoteMessage;
import com.xingqiao.order.service.WebSocketService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 股票行情查询消息消费者
 * 用于接收和处理订单创建消息
 *
 * @author xingqiao
 * @date 2025-09-22
 */
@Component
@RocketMQMessageListener(topic = "quote_topic", selectorExpression = "quote_query", consumerGroup = "quote_query_group")
public class QuoteQueryConsumer implements RocketMQListener<MessageExt> {

    private static final Logger logger = LoggerFactory.getLogger(QuoteQueryConsumer.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private QuoteApiStrategyFactory quoteApiStrategyFactory;

    @Autowired
    private WebSocketService webSocketService;


    @Override
    public void onMessage(MessageExt message) {
        RocketMQConsumerHelper.safeHandleMessage(message, msg -> {
            // 获取消息体内容
            String messageBody = RocketMQConsumerHelper.getMessageBodyAsString(msg);
            logger.warn("开始处理股票行情查询请求: {}", messageBody);

            // 解析消息内容为QuoteMessage对象
            QuoteMessage quoteMessage = JSON.parseObject(messageBody, QuoteMessage.class);
            // 处理订单创建逻辑
            processQuoteQuery(quoteMessage);
        });
    }

    private void processQuoteQuery(QuoteMessage quoteMessage) {

        try {
            // 1. 获取所有订阅的股票信息
            List<QueryStockQuote> subscribedStocks = getAllSubscribedStocks(quoteMessage);
            if (subscribedStocks.isEmpty()) {
                logger.warn("当前没有订阅的股票");
                return;
            }
            // 2. 获取行情数据
            R<List<StockQuote>> result = quoteApiStrategyFactory.getStockQuoteList(subscribedStocks);
            if (R.isSuccess(result) && result.getData() != null) {
                // 3. 推送行情数据给前端
                pushStockQuotesToFrontend(quoteMessage, result.getData());
            }
            logger.info("发现 {} 只订阅的股票需要查询行情", subscribedStocks.size());

        } catch (Exception e) {
            logger.error("处理股票行情查询请求异常", e);
        }
    }

    /**
     * 获取所有订阅的股票信息
     */
    private List<QueryStockQuote> getAllSubscribedStocks(QuoteMessage quoteMessage) {
        List<QueryStockQuote> subscribedStocks = new ArrayList<>();
        try {
            Set<String> subscribedStocksSet = redisService.getCacheSet(TradeConstants.STOCK_INFO_PREFIX + quoteMessage.getSessionId());
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
            logger.error("获取订阅股票列表异常", e);
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
            logger.info("成功推送股票行情数据到前端");
        } catch (Exception e) {
            logger.error("推送股票行情数据到前端异常", e);
        }
    }
}