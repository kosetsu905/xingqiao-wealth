package com.xingqiao.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.xingqiao.system.domain.SysEmployeeQualifications;
import com.xingqiao.system.service.ISysEmployeeQualificationsService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 员工资质认证Controller
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@RestController
@RequestMapping("/qualifications")
public class SysEmployeeQualificationsController extends BaseController
{
    @Autowired
    private ISysEmployeeQualificationsService sysEmployeeQualificationsService;

    /**
     * 查询员工资质认证列表
     */
    @RequiresPermissions("system:qualifications:list")
    @GetMapping("/list")
    public TableDataInfo list(SysEmployeeQualifications sysEmployeeQualifications)
    {
        startPage();
        List<SysEmployeeQualifications> list = sysEmployeeQualificationsService.selectSysEmployeeQualificationsList(sysEmployeeQualifications);
        return getDataTable(list);
    }

    /**
     * 导出员工资质认证列表
     */
    @RequiresPermissions("system:qualifications:export")
    @Log(title = "员工资质认证", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysEmployeeQualifications sysEmployeeQualifications)
    {
        List<SysEmployeeQualifications> list = sysEmployeeQualificationsService.selectSysEmployeeQualificationsList(sysEmployeeQualifications);
        ExcelUtil<SysEmployeeQualifications> util = new ExcelUtil<SysEmployeeQualifications>(SysEmployeeQualifications.class);
        util.exportExcel(response, list, "员工资质认证数据");
    }

    /**
     * 获取员工资质认证详细信息
     */
    @RequiresPermissions("system:qualifications:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysEmployeeQualificationsService.selectSysEmployeeQualificationsById(id));
    }

    /**
     * 新增员工资质认证
     */
    @RequiresPermissions("system:qualifications:add")
    @Log(title = "员工资质认证", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysEmployeeQualifications sysEmployeeQualifications)
    {
        return toAjax(sysEmployeeQualificationsService.insertSysEmployeeQualifications(sysEmployeeQualifications));
    }

    /**
     * 修改员工资质认证
     */
    @RequiresPermissions("system:qualifications:edit")
    @Log(title = "员工资质认证", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysEmployeeQualifications sysEmployeeQualifications)
    {
        return toAjax(sysEmployeeQualificationsService.updateSysEmployeeQualifications(sysEmployeeQualifications));
    }

    /**
     * 删除员工资质认证
     */
    @RequiresPermissions("system:qualifications:remove")
    @Log(title = "员工资质认证", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysEmployeeQualificationsService.deleteSysEmployeeQualificationsByIds(ids));
    }
}
