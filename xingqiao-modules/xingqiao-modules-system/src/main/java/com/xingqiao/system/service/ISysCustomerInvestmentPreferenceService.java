package com.xingqiao.system.service;


import java.util.List;
import com.xingqiao.system.domain.SysCustomerInvestmentPreference;

/**
 * 客户投资偏好Service接口
 *
 * @author xingqiao
 * @date 2025-08-23
 */
public interface ISysCustomerInvestmentPreferenceService
{
    /**
     * 查询客户投资偏好
     *
     * @param id 客户投资偏好主键
     * @return 客户投资偏好
     */
    public SysCustomerInvestmentPreference selectSysCustomerInvestmentPreferenceById(Long id);

    /**
     * 查询客户投资偏好列表
     *
     * @param sysCustomerInvestmentPreference 客户投资偏好
     * @return 客户投资偏好集合
     */
    public List<SysCustomerInvestmentPreference> selectSysCustomerInvestmentPreferenceList(SysCustomerInvestmentPreference sysCustomerInvestmentPreference);

    /**
     * 新增客户投资偏好
     *
     * @param sysCustomerInvestmentPreference 客户投资偏好
     * @return 结果
     */
    public int insertSysCustomerInvestmentPreference(SysCustomerInvestmentPreference sysCustomerInvestmentPreference);

    /**
     * 修改客户投资偏好
     *
     * @param sysCustomerInvestmentPreference 客户投资偏好
     * @return 结果
     */
    public int updateSysCustomerInvestmentPreference(SysCustomerInvestmentPreference sysCustomerInvestmentPreference);

    /**
     * 批量删除客户投资偏好
     *
     * @param ids 需要删除的客户投资偏好主键集合
     * @return 结果
     */
    public int deleteSysCustomerInvestmentPreferenceByIds(Long[] ids);

    /**
     * 删除客户投资偏好信息
     *
     * @param id 客户投资偏好主键
     * @return 结果
     */
    public int deleteSysCustomerInvestmentPreferenceById(Long id);
}
