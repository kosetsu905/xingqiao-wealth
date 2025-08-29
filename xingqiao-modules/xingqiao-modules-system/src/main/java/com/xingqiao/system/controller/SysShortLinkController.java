package com.xingqiao.system.controller;

import java.util.List;
import java.util.UUID;

import com.xingqiao.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.system.domain.SysShortLink;
import com.xingqiao.system.service.ISysShortLinkService;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;
import com.xingqiao.common.log.annotation.Log;
import javax.servlet.http.HttpServletResponse;

/**
 * 短链接Controller
 * 
 * @author xingqiao
 */
@RestController
@RequestMapping("/shortlink")
public class SysShortLinkController extends BaseController
{
    @Autowired
    private ISysShortLinkService sysShortLinkService;

    /**
     * 查询短链接列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysShortLink sysShortLink)
    {
        startPage();
        List<SysShortLink> list = sysShortLinkService.selectSysShortLinkList(sysShortLink);
        return getDataTable(list);
    }

    /**
     * 导出短链接列表
     */
    @Log(title = "短链接", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, SysShortLink sysShortLink)
    {
        List<SysShortLink> list = sysShortLinkService.selectSysShortLinkList(sysShortLink);
        ExcelUtil<SysShortLink> util = new ExcelUtil<SysShortLink>(SysShortLink.class);
        util.exportExcel(response, list, "短链接数据");
    }

    /**
     * 获取短链接详细信息
     */
    @GetMapping(value = "/{shortLinkId}")
    public AjaxResult getInfo(@PathVariable("shortLinkId") Long shortLinkId)
    {
        return AjaxResult.success(sysShortLinkService.selectSysShortLinkByShortLinkId(shortLinkId));
    }

    /**
     * 新增短链接
     */
    @Log(title = "短链接", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysShortLink sysShortLink)
    {
        // 生成短链接码
        if (sysShortLink.getShortCode() == null || sysShortLink.getShortCode().isEmpty()) {
            sysShortLink.setShortCode(generateShortCode());
        }
        // 初始化点击次数
        sysShortLink.setClickCount(0L);
        return toAjax(sysShortLinkService.insertSysShortLink(sysShortLink));
    }

    /**
     * 修改短链接
     */
    @Log(title = "短链接", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysShortLink sysShortLink)
    {
        return toAjax(sysShortLinkService.updateSysShortLink(sysShortLink));
    }

    /**
     * 删除短链接
     */
    @Log(title = "短链接", businessType = BusinessType.DELETE)
	@DeleteMapping("/{shortLinkIds}")
    public AjaxResult remove(@PathVariable Long[] shortLinkIds)
    {
        return toAjax(sysShortLinkService.deleteSysShortLinkByShortLinkIds(shortLinkIds));
    }

    /**
     * 生成短链接码
     * @return 短链接码
     */
    private String generateShortCode() {
        // 使用UUID生成短链接码，取前8位
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}