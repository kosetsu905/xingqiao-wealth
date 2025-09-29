package com.xingqiao.order.rocketmq.service.impl;

import com.xingqiao.common.rocketmq.service.RocketMQMessageService;
import com.xingqiao.order.rocketmq.domain.QuoteMessage;
import com.xingqiao.order.rocketmq.service.QuoteMessageService;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 行情消息服务实现类
 *
 * @author yanghua
 */
@Service
public class QuoteMessageServiceImpl implements QuoteMessageService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteMessageServiceImpl.class);

    @Autowired
    private RocketMQMessageService rocketMQMessageService;
    // 行情查询通知主题名称
    private static final String QUOTE_TOPIC = "quote_topic";
    // 行情查询通知标签名称
    private static final String QUOTE_QUERY_TAG = "quote_query";

    @Override
    public SendResult sendQuoteQueryMessage(QuoteMessage message) {
        logger.info("开始行情查询通知创建消息");
        try {
            SendResult result = rocketMQMessageService.sendSyncMessage(QUOTE_TOPIC, QUOTE_QUERY_TAG, message);
            logger.info("行情查询通知创建消息发送成功， 发送结果: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("行情查询通知消息发送失败", e);
            throw new RuntimeException("行情查询通知消息发送失败", e);
        }
    }
}
