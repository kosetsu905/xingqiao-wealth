package com.xingqiao.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.domain.AgencyEkyc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xingqiao.common.log.annotation.Log;
import com.xingqiao.common.log.enums.BusinessType;
import com.xingqiao.common.security.annotation.RequiresPermissions;
import com.xingqiao.system.domain.SysEmployeeInfo;
import com.xingqiao.system.service.ISysEmployeeInfoService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 员工信息Controller
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@RestController
@RequestMapping("/agency/employ")
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
    @GetMapping(value = "/getInfo")
    public AjaxResult getInfo()
    {
        Long userId = SecurityUtils.getUserId();
        return success(sysEmployeeInfoService.selectSysEmployeeInfo(userId));
    }

}
