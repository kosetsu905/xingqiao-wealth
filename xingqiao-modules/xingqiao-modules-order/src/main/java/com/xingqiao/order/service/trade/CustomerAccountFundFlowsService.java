package com.xingqiao.order.service.trade;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.order.mapper.CustomerAccountFundFlowsMapper;
import com.xingqiao.order.domain.CustomerAccountFundFlows;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.common.core.web.page.TableDataInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 账户资金流水Service业务层处理
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
@Service
public class CustomerAccountFundFlowsService
{
    @Autowired
    private CustomerAccountFundFlowsMapper customerAccountFundFlowsMapper;

    /**
     * 查询账户资金流水
     * 
     * @param id 账户资金流水主键
     * @return 账户资金流水
     */
    public CustomerAccountFundFlows selectCustomerAccountFundFlowsById(String id)
    {
        return customerAccountFundFlowsMapper.selectCustomerAccountFundFlowsById(id);
    }

    /**
     * 查询账户资金流水列表
     * 
     * @param customerAccountFundFlows 账户资金流水
     * @return 账户资金流水集合
     */
    public List<CustomerAccountFundFlows> selectCustomerAccountFundFlowsList(CustomerAccountFundFlows customerAccountFundFlows)
    {
        return customerAccountFundFlowsMapper.selectCustomerAccountFundFlowsList(customerAccountFundFlows);
    }

    /**
     * 查询账户资金流水列表（分页）
     * 
     * @param customerAccountFundFlows 账户资金流水
     * @return 账户资金流水分页数据
     */
    public TableDataInfo selectCustomerAccountFundFlowsPage(CustomerAccountFundFlows customerAccountFundFlows)
    {
        List<CustomerAccountFundFlows> list = customerAccountFundFlowsMapper.selectCustomerAccountFundFlowsList(customerAccountFundFlows);
        return new TableDataInfo(list, list.size());
    }

    /**
     * 根据用户ID查询账户资金流水
     * 
     * @param userId 用户ID
     * @return 账户资金流水集合
     */
    public List<CustomerAccountFundFlows> selectCustomerAccountFundFlowsByUserId(String userId)
    {
        CustomerAccountFundFlows customerAccountFundFlows = new CustomerAccountFundFlows();
        customerAccountFundFlows.setUserId(userId);
        return customerAccountFundFlowsMapper.selectCustomerAccountFundFlowsList(customerAccountFundFlows);
    }

    /**
     * 根据账户ID查询账户资金流水
     * 
     * @param accountId 账户ID
     * @return 账户资金流水集合
     */
    public List<CustomerAccountFundFlows> selectCustomerAccountFundFlowsByAccountId(String accountId)
    {
        CustomerAccountFundFlows customerAccountFundFlows = new CustomerAccountFundFlows();
        customerAccountFundFlows.setAccountId(accountId);
        return customerAccountFundFlowsMapper.selectCustomerAccountFundFlowsList(customerAccountFundFlows);
    }

    /**
     * 根据流水类型查询账户资金流水
     * 
     * @param flowType 流水类型
     * @return 账户资金流水集合
     */
    public List<CustomerAccountFundFlows> selectCustomerAccountFundFlowsByFlowType(Long flowType)
    {
        CustomerAccountFundFlows customerAccountFundFlows = new CustomerAccountFundFlows();
        customerAccountFundFlows.setFlowType(flowType);
        return customerAccountFundFlowsMapper.selectCustomerAccountFundFlowsList(customerAccountFundFlows);
    }

    /**
     * 新增账户资金流水
     * 
     * @param customerAccountFundFlows 账户资金流水
     * @return 结果
     */
    public int insertCustomerAccountFundFlows(CustomerAccountFundFlows customerAccountFundFlows)
    {
        customerAccountFundFlows.setCreateTime(DateUtils.getNowDate());
        return customerAccountFundFlowsMapper.insertCustomerAccountFundFlows(customerAccountFundFlows);
    }

    /**
     * 修改账户资金流水
     * 
     * @param customerAccountFundFlows 账户资金流水
     * @return 结果
     */
    public int updateCustomerAccountFundFlows(CustomerAccountFundFlows customerAccountFundFlows)
    {
        customerAccountFundFlows.setUpdateTime(DateUtils.getNowDate());
        return customerAccountFundFlowsMapper.updateCustomerAccountFundFlows(customerAccountFundFlows);
    }

    /**
     * 批量删除账户资金流水
     * 
     * @param ids 需要删除的账户资金流水主键集合
     * @return 结果
     */
    public int deleteCustomerAccountFundFlowsByIds(String[] ids)
    {
        return customerAccountFundFlowsMapper.deleteCustomerAccountFundFlowsByIds(ids);
    }

    /**
     * 删除账户资金流水信息
     * 
     * @param id 账户资金流水主键
     * @return 结果
     */
    public int deleteCustomerAccountFundFlowsById(String id)
    {
        return customerAccountFundFlowsMapper.deleteCustomerAccountFundFlowsById(id);
    }
}