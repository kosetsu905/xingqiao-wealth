package com.xingqiao.order.rocketmq.service;

import com.xingqiao.order.rocketmq.domain.QuoteMessage;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * 行情消息服务
 */
public interface QuoteMessageService {

    /**
     * 发送触发股票行情查询和推送消息
     *
     * @param orderMessage 订单消息
     * @return 发送结果
     */
    SendResult sendQuoteQueryMessage(QuoteMessage orderMessage);

}
