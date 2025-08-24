package com.xingqiao.system.service;

import java.util.List;
import com.xingqiao.system.domain.SysCustomerFinancial;

/**
 * 客户财务信息Service接口
 *
 * @author xingqiao
 * @date 2025-08-23
 */
public interface ISysCustomerFinancialService
{
    /**
     * 查询客户财务信息
     *
     * @param id 客户财务信息主键
     * @return 客户财务信息
     */
    public SysCustomerFinancial selectSysCustomerFinancialById(Long id);

    /**
     * 查询客户财务信息列表
     *
     * @param sysCustomerFinancial 客户财务信息
     * @return 客户财务信息集合
     */
    public List<SysCustomerFinancial> selectSysCustomerFinancialList(SysCustomerFinancial sysCustomerFinancial);

    /**
     * 新增客户财务信息
     *
     * @param sysCustomerFinancial 客户财务信息
     * @return 结果
     */
    public int insertSysCustomerFinancial(SysCustomerFinancial sysCustomerFinancial);

    /**
     * 修改客户财务信息
     *
     * @param sysCustomerFinancial 客户财务信息
     * @return 结果
     */
    public int updateSysCustomerFinancial(SysCustomerFinancial sysCustomerFinancial);

    /**
     * 批量删除客户财务信息
     *
     * @param ids 需要删除的客户财务信息主键集合
     * @return 结果
     */
    public int deleteSysCustomerFinancialByIds(Long[] ids);

    /**
     * 删除客户财务信息信息
     *
     * @param id 客户财务信息主键
     * @return 结果
     */
    public int deleteSysCustomerFinancialById(Long id);
}
