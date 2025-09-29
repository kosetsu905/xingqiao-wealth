package com.xingqiao.order.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 交易API策略工厂类
 * 负责管理所有的TradeApiStrategy实现，并根据策略名称获取对应的策略
 * 
 * @author xingqiao
 * @date 2025-09-23
 */
@Component
public class TradeApiStrategyFactory {

    @Autowired
    private Set<TradeApiStrategy> tradeApiStrategies;

    private final Map<String, TradeApiStrategy> strategyMap = new HashMap<>();

    private TradeApiStrategy defaultStrategy;

    /**
     * 初始化策略工厂
     * 将所有的TradeApiStrategy实现放入Map中，并设置默认策略
     */
    @PostConstruct
    public void init() {
        for (TradeApiStrategy strategy : tradeApiStrategies) {
            String strategyName = strategy.getStrategyName();
            strategyMap.put(strategyName, strategy);
            // 设置默认策略
            if ("default".equals(strategyName)) {
                defaultStrategy = strategy;
            }
        }
    }

    /**
     * 根据策略名称获取交易API策略
     * @param strategyName 策略名称
     * @return 交易API策略实现
     */
    public TradeApiStrategy getStrategy(String strategyName) {
        if (strategyName != null && strategyMap.containsKey(strategyName)) {
            return strategyMap.get(strategyName);
        }
        // 如果找不到指定的策略，返回默认策略
        return defaultStrategy;
    }

    /**
     * 获取默认的交易API策略
     * @return 默认交易API策略实现
     */
    public TradeApiStrategy getDefaultStrategy() {
        return defaultStrategy;
    }
}