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
import com.xingqiao.system.domain.SysFile;
import com.xingqiao.system.service.ISysFileService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 系统文件管理Controller
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@RestController
@RequestMapping("/file")
public class SysFileController extends BaseController
{
    @Autowired
    private ISysFileService sysFileService;

    /**
     * 查询系统文件管理列表
     */
    @RequiresPermissions("system:file:list")
    @GetMapping("/list")
    public TableDataInfo list(SysFile sysFile)
    {
        startPage();
        List<SysFile> list = sysFileService.selectSysFileList(sysFile);
        return getDataTable(list);
    }

    /**
     * 导出系统文件管理列表
     */
    @RequiresPermissions("system:file:export")
    @Log(title = "系统文件管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysFile sysFile)
    {
        List<SysFile> list = sysFileService.selectSysFileList(sysFile);
        ExcelUtil<SysFile> util = new ExcelUtil<SysFile>(SysFile.class);
        util.exportExcel(response, list, "系统文件管理数据");
    }

    /**
     * 获取系统文件管理详细信息
     */
    @RequiresPermissions("system:file:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysFileService.selectSysFileById(id));
    }

    /**
     * 新增系统文件管理
     */
    @RequiresPermissions("system:file:add")
    @Log(title = "系统文件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysFile sysFile)
    {
        return toAjax(sysFileService.insertSysFile(sysFile));
    }

    /**
     * 修改系统文件管理
     */
    @RequiresPermissions("system:file:edit")
    @Log(title = "系统文件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysFile sysFile)
    {
        return toAjax(sysFileService.updateSysFile(sysFile));
    }

    /**
     * 删除系统文件管理
     */
    @RequiresPermissions("system:file:remove")
    @Log(title = "系统文件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysFileService.deleteSysFileByIds(ids));
    }
}
