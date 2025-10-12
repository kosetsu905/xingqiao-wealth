package com.xingqiao.order.service;

import com.xingqiao.order.domain.CustomerAccounts;
import java.util.List;

public interface ICustomerAccountsService {

    /**
     * 查询账户列表
     */
    List<CustomerAccounts> selectCustomerAccountsList(CustomerAccounts customerAccounts);

    /**
     * 根据ID获取账户
     */
    CustomerAccounts selectCustomerAccountsById(Long id);

    /**
     * 根据用户ID获取账户
     */
    CustomerAccounts selectCustomerAccountsByUserId(Long userId);

    /**
     * 创建新账户（初始化余额为0）
     */
    int createAccount(Long userId, String currency, String remark);

    /**
     * 更新账户信息（带乐观锁）
     */
    int updateCustomerAccounts(CustomerAccounts customerAccounts);

    /**
     * 批量删除账户（ID数组）
     */
    int deleteCustomerAccountsByIds(Long[] ids);

    /**
     * 冻结账户（状态 -> 2）
     */
    int freezeAccount(Long id);

    /**
     * 解冻账户（状态 -> 1）
     */
    int unfreezeAccount(Long id);

    /**
     * 销户（状态 -> 3）
     */
    int closeAccount(Long id);
}