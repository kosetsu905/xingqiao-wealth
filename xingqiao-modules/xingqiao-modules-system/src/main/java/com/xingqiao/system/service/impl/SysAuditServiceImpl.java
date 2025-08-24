package com.xingqiao.system.service.impl;

import java.util.List;
import com.xingqiao.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.SysAuditMapper;
import com.xingqiao.system.domain.SysAudit;
import com.xingqiao.system.service.ISysAuditService;

import javax.annotation.Resource;

/**
 * 系统审核记录Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@Service
public class SysAuditServiceImpl implements ISysAuditService
{
    @Resource
    private SysAuditMapper sysAuditMapper;

    /**
     * 查询系统审核记录
     *
     * @param id 系统审核记录主键
     * @return 系统审核记录
     */
    @Override
    public SysAudit selectSysAuditById(Long id)
    {
        return sysAuditMapper.selectSysAuditById(id);
    }

    /**
     * 查询系统审核记录列表
     *
     * @param sysAudit 系统审核记录
     * @return 系统审核记录
     */
    @Override
    public List<SysAudit> selectSysAuditList(SysAudit sysAudit)
    {
        return sysAuditMapper.selectSysAuditList(sysAudit);
    }

    /**
     * 新增系统审核记录
     *
     * @param sysAudit 系统审核记录
     * @return 结果
     */
    @Override
    public int insertSysAudit(SysAudit sysAudit)
    {
        sysAudit.setCreateTime(DateUtils.getNowDate());
        return sysAuditMapper.insertSysAudit(sysAudit);
    }

    /**
     * 修改系统审核记录
     *
     * @param sysAudit 系统审核记录
     * @return 结果
     */
    @Override
    public int updateSysAudit(SysAudit sysAudit)
    {
        sysAudit.setUpdateTime(DateUtils.getNowDate());
        return sysAuditMapper.updateSysAudit(sysAudit);
    }

    /**
     * 批量删除系统审核记录
     *
     * @param ids 需要删除的系统审核记录主键
     * @return 结果
     */
    @Override
    public int deleteSysAuditByIds(Long[] ids)
    {
        return sysAuditMapper.deleteSysAuditByIds(ids);
    }

    /**
     * 删除系统审核记录信息
     *
     * @param id 系统审核记录主键
     * @return 结果
     */
    @Override
    public int deleteSysAuditById(Long id)
    {
        return sysAuditMapper.deleteSysAuditById(id);
    }
}
