package com.xingqiao.order.mapper;

import java.util.List;
import com.xingqiao.order.domain.CustomerAccounts;

/**
 * 客户资金账户Mapper接口
 *
 * @author xingqiao
 * @date 2025-09-21
 */
public interface CustomerAccountsMapper
{
    /**
     * 查询客户资金账户
     *
     * @param id 客户资金账户主键
     * @return 客户资金账户
     */
    public CustomerAccounts selectCustomerAccountsById(Long id);

    /**
     * 查询客户资金账户列表
     *
     * @param customerAccounts 客户资金账户
     * @return 客户资金账户集合
     */
    public List<CustomerAccounts> selectCustomerAccountsList(CustomerAccounts customerAccounts);

    /**
     * 新增客户资金账户
     *
     * @param customerAccounts 客户资金账户
     * @return 结果
     */
    public int insertCustomerAccounts(CustomerAccounts customerAccounts);

    /**
     * 修改客户资金账户
     *
     * @param customerAccounts 客户资金账户
     * @return 结果
     */
    public int updateCustomerAccounts(CustomerAccounts customerAccounts);

    /**
     * 删除客户资金账户
     *
     * @param id 客户资金账户主键
     * @return 结果
     */
    public int deleteCustomerAccountsById(Long id);

    /**
     * 批量删除客户资金账户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerAccountsByIds(Long[] ids);
}
