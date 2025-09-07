package com.xingqiao.system.mapper.employee;


import java.util.List;
import com.xingqiao.system.domain.employee.SysCustomerInfo;

/**
 * 客户信息Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-23
 */
public interface SysCustomerInfoMapper
{
    /**
     * 查询客户信息
     *
     * @param id 客户信息主键
     * @return 客户信息
     */
    public SysCustomerInfo selectSysCustomerInfoById(Long id);

    /**
     * 查询客户信息列表
     *
     * @param sysCustomerInfo 客户信息
     * @return 客户信息集合
     */
    public List<SysCustomerInfo> selectSysCustomerInfoList(SysCustomerInfo sysCustomerInfo);

    /**
     * 新增客户信息
     *
     * @param sysCustomerInfo 客户信息
     * @return 结果
     */
    public int insertSysCustomerInfo(SysCustomerInfo sysCustomerInfo);

    /**
     * 修改客户信息
     *
     * @param sysCustomerInfo 客户信息
     * @return 结果
     */
    public int updateSysCustomerInfo(SysCustomerInfo sysCustomerInfo);

    /**
     * 删除客户信息
     *
     * @param id 客户信息主键
     * @return 结果
     */
    public int deleteSysCustomerInfoById(Long id);

    /**
     * 批量删除客户信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCustomerInfoByIds(Long[] ids);
}
