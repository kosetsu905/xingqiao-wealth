package com.xingqiao.system.service;

import java.util.List;
import com.xingqiao.system.domain.SysAudit;

/**
 * 系统审核记录Service接口
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public interface ISysAuditService
{
    /**
     * 查询系统审核记录
     *
     * @param id 系统审核记录主键
     * @return 系统审核记录
     */
    public SysAudit selectSysAuditById(Long id);

    /**
     * 查询系统审核记录列表
     *
     * @param sysAudit 系统审核记录
     * @return 系统审核记录集合
     */
    public List<SysAudit> selectSysAuditList(SysAudit sysAudit);

    /**
     * 新增系统审核记录
     *
     * @param sysAudit 系统审核记录
     * @return 结果
     */
    public int insertSysAudit(SysAudit sysAudit);

    /**
     * 修改系统审核记录
     *
     * @param sysAudit 系统审核记录
     * @return 结果
     */
    public int updateSysAudit(SysAudit sysAudit);

    /**
     * 批量删除系统审核记录
     *
     * @param ids 需要删除的系统审核记录主键集合
     * @return 结果
     */
    public int deleteSysAuditByIds(Long[] ids);

    /**
     * 删除系统审核记录信息
     *
     * @param id 系统审核记录主键
     * @return 结果
     */
    public int deleteSysAuditById(Long id);
}
