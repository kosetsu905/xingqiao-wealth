package com.xingqiao.order.quote;


import com.xingqiao.api.trade.domain.SubscribeRequest;
import com.xingqiao.order.rocketmq.domain.QuoteMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 *
 * 负责管理所有的SubscribeStrategy实现，并根据策略名称获取对应的策略
 *
 * @author xingqiao
 * @date 2025-09-23
 */
@Slf4j
@Component
public class SubscribeStrategyFactory {

    @Autowired
    private Set<SubscribeStrategy> quoteApiStrategies;

    private final Map<String, SubscribeStrategy> strategyMap = new HashMap<>();

    /**
     * -- GETTER --
     * 获取默认的行情API策略
     *
     * @return 默认行情API策略实现
     */
    @Getter
    private SubscribeStrategy defaultStrategy;

    /**
     * 初始化策略工厂
     * 将所有的QuoteApiStrategy实现放入Map中，并设置默认策略
     */
    @PostConstruct
    public void init() {
        for (SubscribeStrategy strategy : quoteApiStrategies) {
            String strategyName = strategy.getStrategyName();
            strategyMap.put(strategyName, strategy);
            // 设置默认策略
            if ("default".equals(strategyName)) {
                defaultStrategy = strategy;
            }
        }
    }

    /**
     * 根据策略名称获取行情API策略
     *
     * @param strategyName 策略名称
     * @return 行情API策略实现
     */
    public SubscribeStrategy getStrategy(String strategyName) {
        log.debug("尝试订阅API策略: {}", strategyName);

        if (strategyName != null && strategyMap.containsKey(strategyName)) {
            SubscribeStrategy strategy = strategyMap.get(strategyName);
            log.debug("成功获取策略: {}, 策略实现类: {}", strategyName, strategy.getClass().getSimpleName());
            return strategy;
        }

        // 如果找不到指定的策略，记录警告日志并返回默认策略
        if (strategyName != null) {
            log.warn("找不到指定的策略: {}, 将使用默认策略", strategyName);
        } else {
            log.debug("未指定策略名称，将使用默认策略");
        }

        if (defaultStrategy != null) {
            log.debug("返回默认策略，实现类: {}", defaultStrategy.getClass().getSimpleName());
        } else {
            log.error("默认策略未初始化，返回null");
        }

        return defaultStrategy;
    }

    public void subscribe(SubscribeRequest subscribeRequest) {
        SubscribeStrategy strategy = getStrategy(subscribeRequest.getDataType());
        strategy.subscribe(subscribeRequest);
    }

    public void unsubscribe(SubscribeRequest subscribeRequest) {
        SubscribeStrategy strategy = getStrategy(subscribeRequest.getDataType());
        strategy.unsubscribe(subscribeRequest);
    }

    public void processQuoteQuery(QuoteMessage quoteMessage){
        SubscribeStrategy strategy = getStrategy(quoteMessage.getDataType());
        strategy.processQuoteQuery(quoteMessage);
    }

}
