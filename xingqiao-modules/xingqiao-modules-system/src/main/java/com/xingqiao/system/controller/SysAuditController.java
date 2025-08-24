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
import com.xingqiao.system.domain.SysAudit;
import com.xingqiao.system.service.ISysAuditService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 系统审核记录Controller
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@RestController
@RequestMapping("/audit")
public class SysAuditController extends BaseController
{
    @Autowired
    private ISysAuditService sysAuditService;

    /**
     * 查询系统审核记录列表
     */
    @RequiresPermissions("system:audit:list")
    @GetMapping("/list")
    public TableDataInfo list(SysAudit sysAudit)
    {
        startPage();
        List<SysAudit> list = sysAuditService.selectSysAuditList(sysAudit);
        return getDataTable(list);
    }

    /**
     * 导出系统审核记录列表
     */
    @RequiresPermissions("system:audit:export")
    @Log(title = "系统审核记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAudit sysAudit)
    {
        List<SysAudit> list = sysAuditService.selectSysAuditList(sysAudit);
        ExcelUtil<SysAudit> util = new ExcelUtil<SysAudit>(SysAudit.class);
        util.exportExcel(response, list, "系统审核记录数据");
    }

    /**
     * 获取系统审核记录详细信息
     */
    @RequiresPermissions("system:audit:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysAuditService.selectSysAuditById(id));
    }

    /**
     * 新增系统审核记录
     */
    @RequiresPermissions("system:audit:add")
    @Log(title = "系统审核记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAudit sysAudit)
    {
        return toAjax(sysAuditService.insertSysAudit(sysAudit));
    }

    /**
     * 修改系统审核记录
     */
    @RequiresPermissions("system:audit:edit")
    @Log(title = "系统审核记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAudit sysAudit)
    {
        return toAjax(sysAuditService.updateSysAudit(sysAudit));
    }

    /**
     * 删除系统审核记录
     */
    @RequiresPermissions("system:audit:remove")
    @Log(title = "系统审核记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysAuditService.deleteSysAuditByIds(ids));
    }
}
