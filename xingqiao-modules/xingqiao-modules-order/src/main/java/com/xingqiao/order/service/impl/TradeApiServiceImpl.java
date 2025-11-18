package com.xingqiao.order.service.impl;

import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.order.service.TradeApiService;
import com.xingqiao.order.trade.TradeApiStrategy;
import com.xingqiao.order.trade.TradeApiStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易API服务实现类
 * 实现TradeApiService接口，提供交易相关的服务
 * 
 * @author xingqiao
 */
@Service
@Slf4j
public class TradeApiServiceImpl implements TradeApiService {

    @Autowired
    private TradeApiStrategyFactory tradeApiStrategyFactory;
    
    /**
     * 创建交易订单
     * @param userId 当前登录用户ID
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    @Override
    public R createTrade(Long userId, TradeRequest tradeRequest) {
        try {
            log.info("创建交易订单: userId={}, request={}", userId, tradeRequest);
            
            // userId不再设置到TradeRequest中，直接传递给策略方法
            
            // 获取默认交易策略
            TradeApiStrategy strategy = tradeApiStrategyFactory.getDefaultStrategy();
            return strategy.createTrade(userId, tradeRequest);
        } catch (Exception e) {
            log.error("创建交易订单失败: {}", e.getMessage(), e);
            return R.fail("创建交易订单失败: " + e.getMessage());
        }
    }
}