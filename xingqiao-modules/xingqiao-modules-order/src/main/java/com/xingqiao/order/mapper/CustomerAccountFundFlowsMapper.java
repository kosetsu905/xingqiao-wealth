package com.xingqiao.order.mapper;


import java.util.List;
import com.xingqiao.order.domain.CustomerAccountFundFlows;

/**
 * 账户资金流水Mapper接口
 *
 * @author xingqiao
 * @date 2025-09-21
 */
public interface CustomerAccountFundFlowsMapper
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
     * 删除账户资金流水
     *
     * @param id 账户资金流水主键
     * @return 结果
     */
    public int deleteCustomerAccountFundFlowsById(String id);

    /**
     * 批量删除账户资金流水
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerAccountFundFlowsByIds(String[] ids);
}
