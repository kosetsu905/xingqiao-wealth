package com.xingqiao.system.mapper;

import java.util.List;
import com.xingqiao.system.domain.SysEmployeeInfo;

/**
 * 员工信息Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public interface SysEmployeeInfoMapper
{
    /**
     * 查询员工信息
     *
     * @param id 员工信息主键
     * @return 员工信息
     */
    public SysEmployeeInfo selectSysEmployeeInfoById(Long id);

    /**
     * 查询员工信息列表
     *
     * @param sysEmployeeInfo 员工信息
     * @return 员工信息集合
     */
    public List<SysEmployeeInfo> selectSysEmployeeInfoList(SysEmployeeInfo sysEmployeeInfo);

    /**
     * 新增员工信息
     *
     * @param sysEmployeeInfo 员工信息
     * @return 结果
     */
    public int insertSysEmployeeInfo(SysEmployeeInfo sysEmployeeInfo);

    /**
     * 修改员工信息
     *
     * @param sysEmployeeInfo 员工信息
     * @return 结果
     */
    public int updateSysEmployeeInfo(SysEmployeeInfo sysEmployeeInfo);

    /**
     * 删除员工信息
     *
     * @param id 员工信息主键
     * @return 结果
     */
    public int deleteSysEmployeeInfoById(Long id);

    /**
     * 批量删除员工信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysEmployeeInfoByIds(Long[] ids);
}
