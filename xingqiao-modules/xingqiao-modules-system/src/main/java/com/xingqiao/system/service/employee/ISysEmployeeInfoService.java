package com.xingqiao.system.service.employee;

import java.util.List;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.system.api.domain.employee.AgencyEkyc;
import com.xingqiao.system.api.model.SysEmployeeInfoResp;
import com.xingqiao.system.domain.employee.SysEmployeeInfo;

/**
 * 员工信息Service接口
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public interface ISysEmployeeInfoService
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
     * 批量删除员工信息
     *
     * @param ids 需要删除的员工信息主键集合
     * @return 结果
     */
    public int deleteSysEmployeeInfoByIds(Long[] ids);

    /**
     * 删除员工信息信息
     *
     * @param id 员工信息主键
     * @return 结果
     */
    public int deleteSysEmployeeInfoById(Long id);

    /**
     * 提交EKYC审核
     * @param ekycData
     * @return
     */
    R submitEkycData(AgencyEkyc ekycData);

    AgencyEkyc getEkycInfo(Long userId);

    SysEmployeeInfoResp selectSysEmployeeInfo(Long userId,Long employeeId);

    int submitAuthInfo(AgencyEkyc.AuthAgencyEkyc ekycData);
}
