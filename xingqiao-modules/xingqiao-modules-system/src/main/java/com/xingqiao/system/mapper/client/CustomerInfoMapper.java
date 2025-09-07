package com.xingqiao.system.mapper.client;


import com.xingqiao.system.domain.client.CustomerInfo;

import java.util.List;

/**
 * 客户信息Mapper接口
 *
 * @author xingqiao
 * @date 2025-09-06
 */
public interface CustomerInfoMapper
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
     * 删除客户信息
     *
     * @param id 客户信息主键
     * @return 结果
     */
    public int deleteCustomerInfoById(Long id);

    /**
     * 批量删除客户信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerInfoByIds(Long[] ids);
}
