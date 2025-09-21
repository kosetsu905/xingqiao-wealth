package com.xingqiao.order.mapper;


import java.util.List;
import com.xingqiao.order.domain.CustomerTradeOrders;

/**
 * 交易订单Mapper接口
 *
 * @author xingqiao
 * @date 2025-09-21
 */
public interface CustomerTradeOrdersMapper
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
     * 删除交易订单
     *
     * @param id 交易订单主键
     * @return 结果
     */
    public int deleteCustomerTradeOrdersById(String id);

    /**
     * 批量删除交易订单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerTradeOrdersByIds(String[] ids);
}
