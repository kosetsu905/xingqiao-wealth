package com.xingqiao.order.quote;

import com.xingqiao.api.trade.domain.SubscribeRequest;
import com.xingqiao.order.rocketmq.domain.QuoteMessage;

public interface SubscribeStrategy {

    /**
     * 订阅
     * @param request 订阅请求
     */
    void subscribe(SubscribeRequest request);

    /**
     * 取消订阅
     * @param request 取消订阅请求
     */
    void unsubscribe(SubscribeRequest request);

    /**
     * 处理订阅并发送给前端
     * @param quoteMessage
     */
    void  processQuoteQuery(QuoteMessage quoteMessage);

    /**
     * 获取策略名称
     * @return 策略名称
     */
    String getStrategyName();
}
