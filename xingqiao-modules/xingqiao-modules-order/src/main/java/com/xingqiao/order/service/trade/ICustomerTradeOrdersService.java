package com.xingqiao.order.service.trade;

import java.math.BigDecimal;
import java.util.List;
import com.xingqiao.order.domain.CustomerTradeOrders;
import com.xingqiao.common.core.web.page.TableDataInfo;
//import com.xingqiao.common.core.domain.AjaxResult;

/**
 * 交易订单Service接口
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
public interface ICustomerTradeOrdersService
{
    /**
     * 查询交易订单
     * 
     * @param id 交易订单主键
     * @return 交易订单
     */
    public CustomerTradeOrders selectCustomerTradeOrdersById(String id);

    /**
     * 查询交易订单列表
     * 
     * @param customerTradeOrders 交易订单
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersList(CustomerTradeOrders customerTradeOrders);

    /**
     * 查询交易订单列表（分页）
     * 
     * @param customerTradeOrders 交易订单
     * @return 交易订单分页数据
     */
    public TableDataInfo selectCustomerTradeOrdersPage(CustomerTradeOrders customerTradeOrders);

    /**
     * 根据用户ID查询交易订单
     * 
     * @param userId 用户ID
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersByUserId(String userId);

    /**
     * 根据账户ID查询交易订单
     * 
     * @param accountId 账户ID
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersByAccountId(String accountId);

    /**
     * 根据证券ID查询交易订单
     * 
     * @param securityId 证券ID
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersBySecurityId(String securityId);

    /**
     * 根据订单状态查询交易订单
     * 
     * @param status 订单状态
     * @return 交易订单集合
     */
    public List<CustomerTradeOrders> selectCustomerTradeOrdersByStatus(Long status);

    /**
     * 新增交易订单
     * 
     * @param customerTradeOrders 交易订单
     * @return 结果
     */
    public int insertCustomerTradeOrders(CustomerTradeOrders customerTradeOrders);

    /**
     * 修改交易订单
     * 
     * @param customerTradeOrders 交易订单
     * @return 结果
     */
    public int updateCustomerTradeOrders(CustomerTradeOrders customerTradeOrders);

    /**
     * 批量删除交易订单
     * 
     * @param ids 需要删除的交易订单主键集合
     * @return 结果
     */
    public int deleteCustomerTradeOrdersByIds(String[] ids);

    /**
     * 删除交易订单信息
     * 
     * @param id 交易订单主键
     * @return 结果
     */
    public int deleteCustomerTradeOrdersById(String id);
    
    /**
     * 获取今日盈亏
     * 
     * @param userId 用户ID
     * @return 今日盈亏金额
     */
    public BigDecimal getTodayProfitLossByUserId(String userId);
}