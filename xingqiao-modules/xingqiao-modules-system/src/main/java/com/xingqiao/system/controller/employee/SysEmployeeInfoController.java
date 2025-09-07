package com.xingqiao.system.controller.employee;

import java.util.Date;
import java.util.List;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.domain.employee.AgencyEkyc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xingqiao.system.domain.employee.SysEmployeeInfo;
import com.xingqiao.system.service.employee.ISysEmployeeInfoService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 员工信息Controller
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@RestController
@RequestMapping("/agency/employee")
public class SysEmployeeInfoController extends BaseController
{
    @Autowired
    private ISysEmployeeInfoService sysEmployeeInfoService;

    /**
     * 查询员工信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysEmployeeInfo sysEmployeeInfo)
    {
        startPage();
        List<SysEmployeeInfo> list = sysEmployeeInfoService.selectSysEmployeeInfoList(sysEmployeeInfo);
        return getDataTable(list);
    }


    /**
     * 获取员工信息详细信息
     */
    @GetMapping(value = "/getInfo/{employeeId}")
    public AjaxResult getInfo(@PathVariable(value = "employeeId", required = false) Long employeeId)
    {
        return success(sysEmployeeInfoService.selectSysEmployeeInfo(null,employeeId));
    }


    /**
     * 获取员工信息详细信息
     */
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo()
    {
        Long userId = SecurityUtils.getUserId();
        return success(sysEmployeeInfoService.selectSysEmployeeInfo(userId,null));
    }


    @PostMapping("/authInfo")
    public R<?> authInfo(@RequestBody AgencyEkyc.AuthAgencyEkyc ekycData) {
        Long userId = SecurityUtils.getUserId();
        ekycData.setAuditorId(userId);
        ekycData.setAuditorName(SecurityUtils.getLoginUser().getUsername());
        ekycData.setAuditTime(new Date());
        return R.ok(sysEmployeeInfoService.submitAuthInfo(ekycData));
    }

}
