package com.xingqiao.order.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.order.mapper.CustomerTradeOrdersMapper;
import com.xingqiao.order.domain.CustomerTradeOrders;
import com.xingqiao.order.service.ICustomerTradeOrdersService;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.common.core.web.page.TableDataInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 交易订单Service业务层处理
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
@Service
public class CustomerTradeOrdersServiceImpl implements ICustomerTradeOrdersService
{
    @Autowired
    private CustomerTradeOrdersMapper customerTradeOrdersMapper;

    /**
     * 查询交易订单
     * 
     * @param id 交易订单主键
     * @return 交易订单
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public int deleteCustomerTradeOrdersById(String id)
    {
        return customerTradeOrdersMapper.deleteCustomerTradeOrdersById(id);
    }
}