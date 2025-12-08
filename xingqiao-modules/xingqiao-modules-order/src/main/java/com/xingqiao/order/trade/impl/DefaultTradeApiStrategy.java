package com.xingqiao.order.trade.impl;

import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.order.domain.CustomerAccounts;
import com.xingqiao.order.domain.CustomerPositions;
import com.xingqiao.order.domain.CustomerTradeOrders;
import com.xingqiao.order.domain.Securities;
import com.xingqiao.order.mapper.SecuritiesMapper;
import com.xingqiao.order.service.FtTrdService;
import com.xingqiao.order.service.trade.CustomerAccountsService;
import com.xingqiao.order.service.trade.CustomerPositionsService;
import com.xingqiao.order.service.trade.CustomerTradeOrdersService;
import com.xingqiao.order.trade.TradeApiStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Date;
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
    
    @Autowired
    private SecuritiesMapper securitiesMapper;
    
    @Autowired
    private CustomerAccountsService customerAccountsService;
    
    @Autowired
    private CustomerPositionsService customerPositionsService;



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
            // 创建交易订单
            CustomerTradeOrders customerTradeOrders = createTradeOrder(userId, tradeRequest);
            
            if (customerTradeOrders != null) {
                // 更新账户资金 - 只处理买入订单（卖出订单不影响资金）
                updateCustomerAccountBalance(tradeRequest, customerTradeOrders);
                
                // 更新持仓表
                updateCustomerPositions(userId, tradeRequest.getAccountId(), customerTradeOrders.getSecurityId(), 
                                       tradeRequest.getDirection(), customerTradeOrders.getQuantity(), customerTradeOrders.getPrice());
                
                return R.ok("创建交易订单成功", customerTradeOrders.getId());
            } else {
                return R.fail("创建交易订单失败：数据库插入失败");
            }
        } catch (Exception e) {
            return R.fail("创建交易订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 创建交易订单
     * @param userId 用户ID
     * @param tradeRequest 交易请求参数
     * @return 创建成功的交易订单，失败返回null
     */
    private CustomerTradeOrders createTradeOrder(Long userId, TradeRequest tradeRequest) {
        // 创建CustomerTradeOrders对象并设置属性
        CustomerTradeOrders customerTradeOrders = new CustomerTradeOrders();
        
        // 设置客户ID
        customerTradeOrders.setUserId(String.valueOf(userId));
        
        // 设置资金账户ID（Long转换为String）
        customerTradeOrders.setAccountId(tradeRequest.getAccountId() != null ? String.valueOf(tradeRequest.getAccountId()) : null);
        
        // 证券ID处理逻辑
        String securityId = null;
        
        // 优先通过securityCode查询securityId（直接使用DAO层）
        if (tradeRequest.getSecurityCode() != null && !tradeRequest.getSecurityCode().isEmpty()) {
            // 创建查询条件
            Securities securities = new Securities();
            
            // 解析securityCode格式，处理BABA.NYSE格式的输入
            String inputCode = tradeRequest.getSecurityCode();
            String codePart = inputCode;
            String exchangePart = null;
            
            // 检查是否包含交易所信息（格式：代码.交易所）
            if (inputCode.contains(".")) {
                int dotIndex = inputCode.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < inputCode.length() - 1) {
                    codePart = inputCode.substring(0, dotIndex);
                    exchangePart = inputCode.substring(dotIndex + 1);
                }
            }
            
            // 设置查询条件
            securities.setSecurityCode(codePart);
            if (exchangePart != null && !exchangePart.isEmpty()) {
                securities.setExchangeCode(exchangePart);
            }
            
            // 使用Mapper直接查询证券信息
            java.util.List<Securities> securitiesList = securitiesMapper.selectSecuritiesList(securities);
            if (securitiesList != null && !securitiesList.isEmpty()) {
                // 获取第一个匹配的证券ID
                securityId = String.valueOf(securitiesList.get(0).getId());
            }
        }
        
        // 设置证券ID
        customerTradeOrders.setSecurityId(securityId);
        
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
        
        // 设置订单时间为当前时间
        customerTradeOrders.setOrderTime(new Date());
        
        // 设置备注信息
        customerTradeOrders.setRemark(tradeRequest.getRemark());
        
        // 调用CustomerTradeOrdersService保存订单
        int result = customerTradeOrdersService.insertCustomerTradeOrders(customerTradeOrders);
        
        return result > 0 ? customerTradeOrders : null;
    }
    
    /**
     * 更新客户账户资金
     * @param tradeRequest 交易请求参数
     * @param customerTradeOrders 交易订单信息
     */
    private void updateCustomerAccountBalance(TradeRequest tradeRequest, CustomerTradeOrders customerTradeOrders) {
        // 更新账户资金 - 只处理买入订单（卖出订单不影响资金）
        if (tradeRequest.getDirection() != null && tradeRequest.getDirection() == 1) { // 1表示买入
            try {
                // 获取账户信息
                Long accountId = tradeRequest.getAccountId();
                if (accountId != null) {
                    CustomerAccounts account = customerAccountsService.selectCustomerAccountsById(accountId);
                    if (account != null) {
                        BigDecimal orderAmount = customerTradeOrders.getAmount();
                        if (orderAmount != null) {
                            // 检查可用资金是否足够
                            if (account.getAvailableBalance().compareTo(orderAmount) >= 0) {
                                // 减少可用资金
                                account.setAvailableBalance(account.getAvailableBalance().subtract(orderAmount));
                                // 增加冻结资金
                                account.setFrozenBalance(account.getFrozenBalance().add(orderAmount));
                                // 更新账户信息
                                customerAccountsService.updateCustomerAccounts(account);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // 记录资金更新异常，但不影响订单创建
                System.err.println("更新账户资金时发生异常：" + e.getMessage());
            }
        }
    }
    
    /**
     * 更新客户持仓信息
     * @param userId 用户ID
     * @param accountId 账户ID
     * @param securityId 证券ID
     * @param direction 交易方向(1-买入, 2-卖出)
     * @param quantity 交易数量
     * @param price 交易价格
     */
    private void updateCustomerPositions(Long userId, Long accountId, String securityId, Integer direction, BigDecimal quantity, BigDecimal price) {
        try {
            // 获取账户ID和证券ID
            String accountIdStr = String.valueOf(accountId);
            
            // 持仓类型默认为普通持仓(1)
            Long positionType = 1L;
            
            // 根据账户ID、证券ID和持仓类型查询是否已存在持仓记录
            CustomerPositions positions = customerPositionsService.selectCustomerPositionByAccountSecurityType(accountIdStr, securityId, positionType);
            
            if (direction != null) {
                if (direction == 1) { // 买入订单 - 增加持仓
                    if (positions != null) {
                        // 已存在持仓记录，更新持仓信息
                        // 计算新的平均成本价 = (原总投入金额 + 新投入金额) / (原持仓数量 + 新买入数量)
                        BigDecimal originalTotalCost = positions.getAvgCostPrice().multiply(positions.getQuantity());
                        BigDecimal newTotalCost = price.multiply(quantity);
                        BigDecimal newTotalQuantity = positions.getQuantity().add(quantity);
                        BigDecimal newAvgCostPrice = originalTotalCost.add(newTotalCost).divide(newTotalQuantity, 2, BigDecimal.ROUND_HALF_UP);
                        
                        // 更新持仓数量和可用数量
                        positions.setQuantity(newTotalQuantity);
                        positions.setAvailableQuantity(positions.getAvailableQuantity().add(quantity));
                        positions.setAvgCostPrice(newAvgCostPrice);
                        
                        // 更新持仓记录
                        customerPositionsService.updateCustomerPositions(positions);
                    } else {
                        // 不存在持仓记录，创建新的持仓记录
                        CustomerPositions newPositions = new CustomerPositions();
                        newPositions.setUserId(String.valueOf(userId));
                        newPositions.setAccountId(accountIdStr);
                        newPositions.setSecurityId(securityId);
                        newPositions.setPositionType(positionType);
                        newPositions.setQuantity(quantity);
                        newPositions.setAvailableQuantity(quantity);
                        newPositions.setFrozenQuantity(BigDecimal.ZERO);
                        newPositions.setAvgCostPrice(price);
                        newPositions.setVersion(1L);
                        
                        // 创建持仓记录
                        customerPositionsService.insertCustomerPositions(newPositions);
                    }
                } else if (direction == 2) { // 卖出订单 - 冻结持仓
                    if (positions != null) {
                        // 检查可用数量是否足够
                        if (positions.getAvailableQuantity().compareTo(quantity) >= 0) {
                            // 减少可用数量，增加冻结数量
                            positions.setAvailableQuantity(positions.getAvailableQuantity().subtract(quantity));
                            positions.setFrozenQuantity(positions.getFrozenQuantity().add(quantity));
                            
                            // 更新持仓记录
                            customerPositionsService.updateCustomerPositions(positions);
                        } else {
                            System.err.println("可用持仓数量不足，无法卖出");
                        }
                    } else {
                        System.err.println("未找到对应的持仓记录，无法卖出");
                    }
                }
            }
        } catch (Exception e) {
            // 记录持仓更新异常，但不影响订单创建
            System.err.println("更新持仓表时发生异常：" + e.getMessage());
        }
    }

}