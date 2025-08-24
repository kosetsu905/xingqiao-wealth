package com.xingqiao.system.service.impl;

import java.util.List;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.system.mapper.SysEmployeeQualificationsExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.SysEmployeeQualificationsMapper;
import com.xingqiao.system.domain.SysEmployeeQualifications;
import com.xingqiao.system.service.ISysEmployeeQualificationsService;

import javax.annotation.Resource;

/**
 * 员工资质认证Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@Service
public class SysEmployeeQualificationsServiceImpl implements ISysEmployeeQualificationsService
{
    @Resource
    private SysEmployeeQualificationsMapper sysEmployeeQualificationsMapper;
    @Resource
    private SysEmployeeQualificationsExtMapper extMapper;

    /**
     * 查询员工资质认证
     *
     * @param id 员工资质认证主键
     * @return 员工资质认证
     */
    @Override
    public SysEmployeeQualifications selectSysEmployeeQualificationsById(Long id)
    {
        return sysEmployeeQualificationsMapper.selectSysEmployeeQualificationsById(id);
    }

    /**
     * 查询员工资质认证列表
     *
     * @param sysEmployeeQualifications 员工资质认证
     * @return 员工资质认证
     */
    @Override
    public List<SysEmployeeQualifications> selectSysEmployeeQualificationsList(SysEmployeeQualifications sysEmployeeQualifications)
    {
        return sysEmployeeQualificationsMapper.selectSysEmployeeQualificationsList(sysEmployeeQualifications);
    }

    /**
     * 新增员工资质认证
     *
     * @param sysEmployeeQualifications 员工资质认证
     * @return 结果
     */
    @Override
    public int insertSysEmployeeQualifications(SysEmployeeQualifications sysEmployeeQualifications)
    {
        sysEmployeeQualifications.setCreateTime(DateUtils.getNowDate());
        return sysEmployeeQualificationsMapper.insertSysEmployeeQualifications(sysEmployeeQualifications);
    }

    /**
     * 修改员工资质认证
     *
     * @param sysEmployeeQualifications 员工资质认证
     * @return 结果
     */
    @Override
    public int updateSysEmployeeQualifications(SysEmployeeQualifications sysEmployeeQualifications)
    {
        sysEmployeeQualifications.setUpdateTime(DateUtils.getNowDate());
        return sysEmployeeQualificationsMapper.updateSysEmployeeQualifications(sysEmployeeQualifications);
    }

    /**
     * 批量删除员工资质认证
     *
     * @param ids 需要删除的员工资质认证主键
     * @return 结果
     */
    @Override
    public int deleteSysEmployeeQualificationsByIds(Long[] ids)
    {
        return sysEmployeeQualificationsMapper.deleteSysEmployeeQualificationsByIds(ids);
    }

    /**
     * 删除员工资质认证信息
     *
     * @param id 员工资质认证主键
     * @return 结果
     */
    @Override
    public int deleteSysEmployeeQualificationsById(Long id)
    {
        return sysEmployeeQualificationsMapper.deleteSysEmployeeQualificationsById(id);
    }

    @Override
    public int deleteByBusinessId(Long businessId) {
        return extMapper.deleteByBusinessId(businessId);
    }
}
