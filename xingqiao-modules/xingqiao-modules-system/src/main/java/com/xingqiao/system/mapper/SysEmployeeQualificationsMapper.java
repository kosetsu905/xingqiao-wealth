package com.xingqiao.system.mapper;

import java.util.List;
import com.xingqiao.system.domain.SysEmployeeQualifications;

/**
 * 员工资质认证Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public interface SysEmployeeQualificationsMapper
{
    /**
     * 查询员工资质认证
     *
     * @param id 员工资质认证主键
     * @return 员工资质认证
     */
    public SysEmployeeQualifications selectSysEmployeeQualificationsById(Long id);

    /**
     * 查询员工资质认证列表
     *
     * @param sysEmployeeQualifications 员工资质认证
     * @return 员工资质认证集合
     */
    public List<SysEmployeeQualifications> selectSysEmployeeQualificationsList(SysEmployeeQualifications sysEmployeeQualifications);

    /**
     * 新增员工资质认证
     *
     * @param sysEmployeeQualifications 员工资质认证
     * @return 结果
     */
    public int insertSysEmployeeQualifications(SysEmployeeQualifications sysEmployeeQualifications);

    /**
     * 修改员工资质认证
     *
     * @param sysEmployeeQualifications 员工资质认证
     * @return 结果
     */
    public int updateSysEmployeeQualifications(SysEmployeeQualifications sysEmployeeQualifications);

    /**
     * 删除员工资质认证
     *
     * @param id 员工资质认证主键
     * @return 结果
     */
    public int deleteSysEmployeeQualificationsById(Long id);

    /**
     * 批量删除员工资质认证
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysEmployeeQualificationsByIds(Long[] ids);
}
