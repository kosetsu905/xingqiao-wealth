package com.xingqiao.order.service;

import java.util.List;
import com.xingqiao.order.domain.CustomerAccountFundFlows;
import com.xingqiao.common.core.web.page.TableDataInfo;
//import com.xingqiao.common.core.domain.AjaxResult;

/**
 * 账户资金流水Service接口
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
public interface ICustomerAccountFundFlowsService
{
    /**
     * 查询账户资金流水
     * 
     * @param id 账户资金流水主键
     * @return 账户资金流水
     */
    public CustomerAccountFundFlows selectCustomerAccountFundFlowsById(String id);

    /**
     * 查询账户资金流水列表
     * 
     * @param customerAccountFundFlows 账户资金流水
     * @return 账户资金流水集合
     */
    public List<CustomerAccountFundFlows> selectCustomerAccountFundFlowsList(CustomerAccountFundFlows customerAccountFundFlows);

    /**
     * 查询账户资金流水列表（分页）
     * 
     * @param customerAccountFundFlows 账户资金流水
     * @return 账户资金流水分页数据
     */
    public TableDataInfo selectCustomerAccountFundFlowsPage(CustomerAccountFundFlows customerAccountFundFlows);

    /**
     * 根据用户ID查询账户资金流水
     * 
     * @param userId 用户ID
     * @return 账户资金流水集合
     */
    public List<CustomerAccountFundFlows> selectCustomerAccountFundFlowsByUserId(String userId);

    /**
     * 根据账户ID查询账户资金流水
     * 
     * @param accountId 账户ID
     * @return 账户资金流水集合
     */
    public List<CustomerAccountFundFlows> selectCustomerAccountFundFlowsByAccountId(String accountId);

    /**
     * 根据流水类型查询账户资金流水
     * 
     * @param flowType 流水类型
     * @return 账户资金流水集合
     */
    public List<CustomerAccountFundFlows> selectCustomerAccountFundFlowsByFlowType(Long flowType);

    /**
     * 新增账户资金流水
     * 
     * @param customerAccountFundFlows 账户资金流水
     * @return 结果
     */
    public int insertCustomerAccountFundFlows(CustomerAccountFundFlows customerAccountFundFlows);

    /**
     * 修改账户资金流水
     * 
     * @param customerAccountFundFlows 账户资金流水
     * @return 结果
     */
    public int updateCustomerAccountFundFlows(CustomerAccountFundFlows customerAccountFundFlows);

    /**
     * 批量删除账户资金流水
     * 
     * @param ids 需要删除的账户资金流水主键集合
     * @return 结果
     */
    public int deleteCustomerAccountFundFlowsByIds(String[] ids);

    /**
     * 删除账户资金流水信息
     * 
     * @param id 账户资金流水主键
     * @return 结果
     */
    public int deleteCustomerAccountFundFlowsById(String id);
}