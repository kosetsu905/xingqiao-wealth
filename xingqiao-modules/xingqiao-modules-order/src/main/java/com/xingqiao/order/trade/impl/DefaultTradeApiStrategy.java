package com.xingqiao.order.trade.impl;

import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.order.domain.CustomerTradeOrders;
import com.xingqiao.order.service.FtTrdService;
import com.xingqiao.order.service.trade.CustomerTradeOrdersService;
import com.xingqiao.order.trade.TradeApiStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    
    @Autowired
    private CustomerTradeOrdersService customerTradeOrdersService;



    /**
     * 创建交易订单
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    /**
     * 创建交易订单
     * @param userId 当前登录用户ID
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    @Override
    public R createTrade(Long userId, TradeRequest tradeRequest) {
        try {
            // 创建CustomerTradeOrders对象并设置属性
            CustomerTradeOrders customerTradeOrders = new CustomerTradeOrders();
            
            // 设置客户ID
            customerTradeOrders.setUserId(String.valueOf(userId));
            
            // 设置资金账户ID（Long转换为String）
            customerTradeOrders.setAccountId(tradeRequest.getAccountId() != null ? String.valueOf(tradeRequest.getAccountId()) : null);
            
            // 设置证券ID（Long转换为String）
            customerTradeOrders.setSecurityId(tradeRequest.getSecurityId() != null ? String.valueOf(tradeRequest.getSecurityId()) : null);
            
            // 设置订单类型（Integer转换为Long）
            if (tradeRequest.getOrderType() != null) {
                customerTradeOrders.setOrderType(tradeRequest.getOrderType().longValue());
            } else {
                customerTradeOrders.setOrderType(1L); // 默认限价单
            }
            
            // 设置买卖方向（Integer转换为Long）
            customerTradeOrders.setDirection(tradeRequest.getDirection() != null ? tradeRequest.getDirection().longValue() : null);
            
            // 设置价格和数量
            customerTradeOrders.setPrice(tradeRequest.getPrice());
            customerTradeOrders.setQuantity(tradeRequest.getQuantity());
            
            // 计算预估金额
            if (tradeRequest.getPrice() != null && tradeRequest.getQuantity() != null) {
                customerTradeOrders.setAmount(tradeRequest.getPrice().multiply(tradeRequest.getQuantity()));
            }
            
            // 设置订单过期时间
            customerTradeOrders.setExpireTime(tradeRequest.getExpireTime());
            
            // 设置条件单相关字段（Integer转换为Long）
            customerTradeOrders.setConditionType(tradeRequest.getConditionType() != null ? tradeRequest.getConditionType().longValue() : null);
            customerTradeOrders.setConditionValue(tradeRequest.getConditionValue());
            
            // 设置订单状态为待报（0）
            customerTradeOrders.setStatus(0L);
            
            // 设置备注信息
            customerTradeOrders.setRemark(tradeRequest.getRemark());
            
            // 调用CustomerTradeOrdersService保存订单
            int result = customerTradeOrdersService.insertCustomerTradeOrders(customerTradeOrders);
            
            if (result > 0) {
                return R.ok("创建交易订单成功", customerTradeOrders.getId());
            } else {
                return R.fail("创建交易订单失败：数据库插入失败");
            }
        } catch (Exception e) {
            return R.fail("创建交易订单失败：" + e.getMessage());
        }
    }



}