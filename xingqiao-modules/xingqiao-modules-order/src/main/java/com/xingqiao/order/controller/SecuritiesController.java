package com.xingqiao.order.controller;

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
import com.xingqiao.order.domain.Securities;
import com.xingqiao.order.service.trade.ISecuritiesService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 证券信息Controller
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@RestController
@RequestMapping("/securities")
public class SecuritiesController extends BaseController
{
    @Autowired
    private ISecuritiesService securitiesService;

    /**
     * 查询证券信息列表
     */
    @RequiresPermissions("system:securities:list")
    @GetMapping("/list")
    public TableDataInfo list(Securities securities)
    {
        startPage();
        List<Securities> list = securitiesService.selectSecuritiesList(securities);
        return getDataTable(list);
    }

    /**
     * 导出证券信息列表
     */
    @RequiresPermissions("system:securities:export")
    @Log(title = "证券信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Securities securities)
    {
        List<Securities> list = securitiesService.selectSecuritiesList(securities);
        ExcelUtil<Securities> util = new ExcelUtil<Securities>(Securities.class);
        util.exportExcel(response, list, "证券信息数据");
    }

    /**
     * 获取证券信息详细信息
     */
    @RequiresPermissions("system:securities:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(securitiesService.selectSecuritiesById(id));
    }

    /**
     * 新增证券信息
     */
    @RequiresPermissions("system:securities:add")
    @Log(title = "证券信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Securities securities)
    {
        return toAjax(securitiesService.insertSecurities(securities));
    }

    /**
     * 修改证券信息
     */
    @RequiresPermissions("system:securities:edit")
    @Log(title = "证券信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Securities securities)
    {
        return toAjax(securitiesService.updateSecurities(securities));
    }

    /**
     * 删除证券信息
     */
    @RequiresPermissions("system:securities:remove")
    @Log(title = "证券信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(securitiesService.deleteSecuritiesByIds(ids));
    }
}
