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
     * 查询交易详情
     * @param tradeId 交易ID
     * @return 交易详情
     */
    @Override
    public R getTradeDetail(Long tradeId) {
        try {
            log.info("查询交易详情: tradeId={}", tradeId);
            // 获取默认交易策略
            TradeApiStrategy strategy = tradeApiStrategyFactory.getDefaultStrategy();
            return strategy.getTradeDetail(tradeId);
        } catch (Exception e) {
            log.error("查询交易详情失败: {}", e.getMessage(), e);
            return R.fail("查询交易详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建交易订单
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    @Override
    public R createTrade(TradeRequest tradeRequest) {
        try {
            log.info("创建交易订单: request={}", tradeRequest);
            // 获取默认交易策略
            TradeApiStrategy strategy = tradeApiStrategyFactory.getDefaultStrategy();
            return strategy.createTrade(tradeRequest);
        } catch (Exception e) {
            log.error("创建交易订单失败: {}", e.getMessage(), e);
            return R.fail("创建交易订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 取消交易订单
     * @param tradeId 交易ID
     * @return 取消结果
     */
    @Override
    public R cancelTrade(Long tradeId) {
        return R.fail("取消交易订单功能暂未实现");
    }
}