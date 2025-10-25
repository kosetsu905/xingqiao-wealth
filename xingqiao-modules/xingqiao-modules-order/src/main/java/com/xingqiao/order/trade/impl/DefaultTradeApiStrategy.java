package com.xingqiao.order.trade.impl;

import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.order.service.FtTrdService;
import com.xingqiao.order.trade.TradeApiStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 默认交易API策略实现类
 * 实现TradeApiStrategy接口，提供基本的交易功能实现
 * 
 * @author xingqiao
 * @date 2025-09-23
 */
@Component
public class DefaultTradeApiStrategy implements TradeApiStrategy {

    @Autowired
    private FtTrdService ftTrdService;

    /**
     * 获取交易详情
     * @param tradeId 交易ID
     * @return 交易详情
     */
    @Override
    public R getTradeDetail(Long tradeId) {
        try {
            // 这里调用FtTrdService获取订单详情
            // 由于FtTrdService返回CompletableFuture<String>，我们需要阻塞等待结果
            CompletableFuture<String> future = ftTrdService.getOrderList(1, 0, 0); // 模拟调用，实际需要根据参数调整
            String result = future.get();
            return R.ok("获取交易详情成功", result);
        } catch (Exception e) {
            return R.fail("获取交易详情失败：" + e.getMessage());
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
            // 这里根据TradeRequest参数调用FtTrdService创建交易订单
            // 由于FtTrdService返回CompletableFuture<String>，我们需要阻塞等待结果
            // 实际实现时需要根据TradeRequest的参数映射到FtTrdService的相应方法
            // 这里简化处理，模拟调用
            CompletableFuture<String> future = ftTrdService.placeOrder(
                    1, 0, 0, 0,
                    tradeRequest.getPrice().intValue(),
                    tradeRequest.getQuantity().intValue(),
                    "0", 0, 0);
            String result = future.get();
            return R.ok("创建交易订单成功", result);
        } catch (Exception e) {
            return R.fail("创建交易订单失败：" + e.getMessage());
        }
    }


    /**
     * 获取策略名称
     * @return 策略名称
     */
    @Override
    public String getStrategyName() {
        return "default";
    }
}