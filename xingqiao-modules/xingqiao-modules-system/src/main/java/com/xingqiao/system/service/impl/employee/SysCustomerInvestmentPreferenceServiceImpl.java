package com.xingqiao.system.service.impl.employee;

import java.util.List;
import com.xingqiao.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.employee.SysCustomerInvestmentPreferenceMapper;
import com.xingqiao.system.domain.employee.SysCustomerInvestmentPreference;
import com.xingqiao.system.service.employee.ISysCustomerInvestmentPreferenceService;

/**
 * 客户投资偏好Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-23
 */
@Service
public class SysCustomerInvestmentPreferenceServiceImpl implements ISysCustomerInvestmentPreferenceService
{
    @Autowired
    private SysCustomerInvestmentPreferenceMapper sysCustomerInvestmentPreferenceMapper;

    /**
     * 查询客户投资偏好
     *
     * @param id 客户投资偏好主键
     * @return 客户投资偏好
     */
    @Override
    public SysCustomerInvestmentPreference selectSysCustomerInvestmentPreferenceById(Long id)
    {
        return sysCustomerInvestmentPreferenceMapper.selectSysCustomerInvestmentPreferenceById(id);
    }

    /**
     * 查询客户投资偏好列表
     *
     * @param sysCustomerInvestmentPreference 客户投资偏好
     * @return 客户投资偏好
     */
    @Override
    public List<SysCustomerInvestmentPreference> selectSysCustomerInvestmentPreferenceList(SysCustomerInvestmentPreference sysCustomerInvestmentPreference)
    {
        return sysCustomerInvestmentPreferenceMapper.selectSysCustomerInvestmentPreferenceList(sysCustomerInvestmentPreference);
    }

    /**
     * 新增客户投资偏好
     *
     * @param sysCustomerInvestmentPreference 客户投资偏好
     * @return 结果
     */
    @Override
    public int insertSysCustomerInvestmentPreference(SysCustomerInvestmentPreference sysCustomerInvestmentPreference)
    {
        sysCustomerInvestmentPreference.setCreateTime(DateUtils.getNowDate());
        return sysCustomerInvestmentPreferenceMapper.insertSysCustomerInvestmentPreference(sysCustomerInvestmentPreference);
    }

    /**
     * 修改客户投资偏好
     *
     * @param sysCustomerInvestmentPreference 客户投资偏好
     * @return 结果
     */
    @Override
    public int updateSysCustomerInvestmentPreference(SysCustomerInvestmentPreference sysCustomerInvestmentPreference)
    {
        sysCustomerInvestmentPreference.setUpdateTime(DateUtils.getNowDate());
        return sysCustomerInvestmentPreferenceMapper.updateSysCustomerInvestmentPreference(sysCustomerInvestmentPreference);
    }

    /**
     * 批量删除客户投资偏好
     *
     * @param ids 需要删除的客户投资偏好主键
     * @return 结果
     */
    @Override
    public int deleteSysCustomerInvestmentPreferenceByIds(Long[] ids)
    {
        return sysCustomerInvestmentPreferenceMapper.deleteSysCustomerInvestmentPreferenceByIds(ids);
    }

    /**
     * 删除客户投资偏好信息
     *
     * @param id 客户投资偏好主键
     * @return 结果
     */
    @Override
    public int deleteSysCustomerInvestmentPreferenceById(Long id)
    {
        return sysCustomerInvestmentPreferenceMapper.deleteSysCustomerInvestmentPreferenceById(id);
    }
}
