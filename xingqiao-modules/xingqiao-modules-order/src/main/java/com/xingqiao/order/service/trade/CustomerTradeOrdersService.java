package com.xingqiao.order.service.trade;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.order.mapper.CustomerTradeOrdersMapper;
import com.xingqiao.order.domain.CustomerTradeOrders;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 交易订单Service业务层处理
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
@Service
public class CustomerTradeOrdersService
{
    @Autowired
    private CustomerTradeOrdersMapper customerTradeOrdersMapper;

    /**
     * 查询交易订单
     * 
     * @param id 交易订单主键
     * @return 交易订单
     */
    public CustomerTradeOrders selectCustomerTradeOrdersById(String id)
    {
        return customerTradeOrdersMapper.selectCustomerTradeOrdersById(id);
    }

    /**
     * 查询交易订单列表
     * 
     * @param customerTradeOrders 交易订单
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersList(CustomerTradeOrders customerTradeOrders)
    {
        return customerTradeOrdersMapper.selectCustomerTradeOrdersList(customerTradeOrders);
    }

    /**
     * 查询交易订单列表（分页）
     * 
     * @param customerTradeOrders 交易订单
     * @return 交易订单分页数据
     */
    public TableDataInfo selectCustomerTradeOrdersPage(CustomerTradeOrders customerTradeOrders)
    {
        List<CustomerTradeOrders> list = customerTradeOrdersMapper.selectCustomerTradeOrdersList(customerTradeOrders);
        return new TableDataInfo(list, list.size());
    }

    /**
     * 根据用户ID查询交易订单
     * 
     * @param userId 用户ID
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersByUserId(String userId)
    {
        CustomerTradeOrders customerTradeOrders = new CustomerTradeOrders();
        customerTradeOrders.setUserId(userId);
        return customerTradeOrdersMapper.selectCustomerTradeOrdersList(customerTradeOrders);
    }

    /**
     * 根据账户ID查询交易订单
     * 
     * @param accountId 账户ID
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersByAccountId(String accountId)
    {
        CustomerTradeOrders customerTradeOrders = new CustomerTradeOrders();
        customerTradeOrders.setAccountId(accountId);
        return customerTradeOrdersMapper.selectCustomerTradeOrdersList(customerTradeOrders);
    }

    /**
     * 根据证券ID查询交易订单
     * 
     * @param securityId 证券ID
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersBySecurityId(String securityId)
    {
        CustomerTradeOrders customerTradeOrders = new CustomerTradeOrders();
        customerTradeOrders.setSecurityId(securityId);
        return customerTradeOrdersMapper.selectCustomerTradeOrdersList(customerTradeOrders);
    }

    /**
     * 根据订单状态查询交易订单
     * 
     * @param status 订单状态
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersByStatus(Long status)
    {
        CustomerTradeOrders customerTradeOrders = new CustomerTradeOrders();
        customerTradeOrders.setStatus(status);
        return customerTradeOrdersMapper.selectCustomerTradeOrdersList(customerTradeOrders);
    }

    /**
     * 新增交易订单
     * 
     * @param customerTradeOrders 交易订单
     * @return 结果
     */
    public int insertCustomerTradeOrders(CustomerTradeOrders customerTradeOrders)
    {
        customerTradeOrders.setCreateTime(DateUtils.getNowDate());
        return customerTradeOrdersMapper.insertCustomerTradeOrders(customerTradeOrders);
    }

    /**
     * 修改交易订单
     * 
     * @param customerTradeOrders 交易订单
     * @return 结果
     */
    public int updateCustomerTradeOrders(CustomerTradeOrders customerTradeOrders)
    {
        customerTradeOrders.setUpdateTime(DateUtils.getNowDate());
        return customerTradeOrdersMapper.updateCustomerTradeOrders(customerTradeOrders);
    }

    /**
     * 批量删除交易订单
     * 
     * @param ids 需要删除的交易订单主键集合
     * @return 结果
     */
    public int deleteCustomerTradeOrdersByIds(String[] ids)
    {
        return customerTradeOrdersMapper.deleteCustomerTradeOrdersByIds(ids);
    }

    /**
     * 删除交易订单信息
     * 
     * @param id 交易订单主键
     * @return 结果
     */
    public int deleteCustomerTradeOrdersById(String id) {
        return customerTradeOrdersMapper.deleteCustomerTradeOrdersById(id);
    }
    
    public BigDecimal getTodayProfitLossByUserId(String userId) {
        // 初始化今日盈亏为0
        BigDecimal todayProfitLoss = BigDecimal.ZERO;
        
        try {
            // 创建查询条件
            CustomerTradeOrders customerTradeOrders = new CustomerTradeOrders();
            customerTradeOrders.setUserId(userId);
            
            // 获取今天的日期范围
            String today = DateUtils.getDate();
            String startDate = today + " 00:00:00";
            String endDate = today + " 23:59:59";
            
            // 查询今日所有已成交的订单
            List<CustomerTradeOrders> todayOrders = customerTradeOrdersMapper.selectCustomerTradeOrdersByUserIdAndDateRange(
                    userId, startDate, endDate);
            
            // 计算今日盈亏
            if (todayOrders != null && !todayOrders.isEmpty()) {
                for (CustomerTradeOrders order : todayOrders) {
                    // 只计算已成交的订单（状态为3：完全成交）
                    if (order.getStatus().equals(3L) && order.getFilledAmount() != null && order.getAmount() != null) {
                        // 买入订单：盈亏 = 已成交金额 - 委托金额（通常为负，因为买入会支出资金）
                        // 卖出订单：盈亏 = 已成交金额 - 委托金额（通常为正，因为卖出会收入资金）
                        BigDecimal orderProfitLoss = order.getFilledAmount().subtract(order.getAmount());
                        todayProfitLoss = todayProfitLoss.add(orderProfitLoss);
                    }
                }
            }
        } catch (Exception e) {
            // 记录异常日志
            e.printStackTrace();
        }
        
        return todayProfitLoss;
    }
}