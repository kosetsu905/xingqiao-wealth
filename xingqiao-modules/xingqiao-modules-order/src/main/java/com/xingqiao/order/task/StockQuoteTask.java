package com.xingqiao.order.task;


import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.config.StockCodeMappingConfig;
import com.xingqiao.order.config.TradeConstants;
import com.xingqiao.order.rocketmq.domain.QuoteMessage;
import com.xingqiao.order.rocketmq.service.QuoteMessageService;
import com.xingqiao.order.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 定时获取股票行情任务
 * 每5秒执行一次
 *
 * @author xingqiao
 * @date 2025-09-24
 */
@Component
public class StockQuoteTask {

    private static final Logger log = LoggerFactory.getLogger(StockQuoteTask.class);

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private QuoteMessageService quoteMessageService;
    @Autowired
    private StockCodeMappingConfig stockCodeMappingConfig;
    


    /**
     * 定时获取股票行情
     * 每30秒执行一次（通过Nacos配置）
     */
    @Scheduled(cron = "${stock.quote.task.cron:*/30 * * * * ?}")
    public void fetchAndPushStockQuotes() {
        // 获取所有会话及其订阅的股票列表
        Set<String> sessionIds = redisService.getCacheSet(TradeConstants.SESSION_KEY_PREFIX);

        if (sessionIds.isEmpty()) {
            log.debug("没有活跃的股票订阅，跳过行情推送");
            return;
        }
        if (!stockCodeMappingConfig.isSwitchOn()){
            log.debug("股票代码映射功能未开启，跳过行情推送");
            return;
        }

        try {
            // 遍历所有订阅的会话和股票
            for (String sessionId : sessionIds) {
                Long userId=webSocketService.getUserBySessionId(sessionId);
                QuoteMessage quoteMessage = new QuoteMessage();
                quoteMessage.setUserId(userId);
                quoteMessage.setSessionId(sessionId);
                quoteMessage.setRemark("触发订阅股票查询");
                quoteMessage.setCreateTime(new Date());
                quoteMessageService.sendQuoteQueryMessage(quoteMessage);
            }
        } catch (Exception e) {
            log.error("执行定时行情推送任务失败", e);
        }
    }

}