package com.xingqiao.system.service.client;


import com.xingqiao.system.domain.client.CustomerInfo;

import java.util.List;

/**
 * 客户信息Service接口
 *
 * @author xingqiao
 * @date 2025-09-06
 */
public interface ICustomerInfoService
{
    /**
     * 查询客户信息
     *
     * @param id 客户信息主键
     * @return 客户信息
     */
    public CustomerInfo selectCustomerInfoById(Long id);

    /**
     * 查询客户信息列表
     *
     * @param customerInfo 客户信息
     * @return 客户信息集合
     */
    public List<CustomerInfo> selectCustomerInfoList(CustomerInfo customerInfo);

    /**
     * 新增客户信息
     *
     * @param customerInfo 客户信息
     * @return 结果
     */
    public int insertCustomerInfo(CustomerInfo customerInfo);

    /**
     * 修改客户信息
     *
     * @param customerInfo 客户信息
     * @return 结果
     */
    public int updateCustomerInfo(CustomerInfo customerInfo);

    /**
     * 批量删除客户信息
     *
     * @param ids 需要删除的客户信息主键集合
     * @return 结果
     */
    public int deleteCustomerInfoByIds(Long[] ids);

    /**
     * 删除客户信息信息
     *
     * @param id 客户信息主键
     * @return 结果
     */
    public int deleteCustomerInfoById(Long id);

    public int authCustomerInfo(CustomerInfo customerInfo);
}
