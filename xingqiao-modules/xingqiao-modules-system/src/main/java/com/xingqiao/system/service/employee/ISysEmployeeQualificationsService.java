package com.xingqiao.system.service.employee;

import java.util.List;
import com.xingqiao.system.domain.employee.SysEmployeeQualifications;

/**
 * 员工资质认证Service接口
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public interface ISysEmployeeQualificationsService
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
     * 批量删除员工资质认证
     *
     * @param ids 需要删除的员工资质认证主键集合
     * @return 结果
     */
    public int deleteSysEmployeeQualificationsByIds(Long[] ids);

    /**
     * 删除员工资质认证信息
     *
     * @param id 员工资质认证主键
     * @return 结果
     */
    public int deleteSysEmployeeQualificationsById(Long id);

    /**
     * 根据业务id删除员工资质认证信息
     * @param businessId
     * @return
     */
    int deleteByBusinessId(Long businessId);
}
