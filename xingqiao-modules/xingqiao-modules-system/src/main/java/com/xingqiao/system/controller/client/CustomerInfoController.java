package com.xingqiao.system.controller.client;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xingqiao.system.domain.client.CustomerInfo;
import com.xingqiao.system.service.client.ICustomerInfoService;
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
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 客户信息Controller
 *
 * @author xingqiao
 * @date 2025-09-08
 */
@RestController
@RequestMapping("/client/customer")
public class CustomerInfoController extends BaseController
{
    @Autowired
    private ICustomerInfoService customerInfoService;

    /**
     * 查询客户信息列表
     */
    @RequiresPermissions("system:client:list")
    @GetMapping("/list")
    public TableDataInfo list(CustomerInfo customerInfo)
    {
        startPage();
        List<CustomerInfo> list = customerInfoService.selectCustomerInfoList(customerInfo);
        return getDataTable(list);
    }

    /**
     * 导出客户信息列表
     */
    @RequiresPermissions("system:client:export")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerInfo customerInfo)
    {
        List<CustomerInfo> list = customerInfoService.selectCustomerInfoList(customerInfo);
        ExcelUtil<CustomerInfo> util = new ExcelUtil<CustomerInfo>(CustomerInfo.class);
        util.exportExcel(response, list, "客户信息数据");
    }

    /**
     * 获取客户信息详细信息
     */
    @RequiresPermissions("system:client:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(customerInfoService.selectCustomerInfoById(id));
    }

    /**
     * 新增客户信息
     */
    @RequiresPermissions("system:client:add")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerInfo customerInfo)
    {
        return toAjax(customerInfoService.insertCustomerInfo(customerInfo));
    }

    /**
     * 修改客户信息
     */
    @RequiresPermissions("system:client:edit")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerInfo customerInfo)
    {
        return toAjax(customerInfoService.updateCustomerInfo(customerInfo));
    }
    /**
     * 审核客户信息
     */
    @RequiresPermissions("system:client:edit")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping("/authInfo")
    public AjaxResult authInfo(@RequestBody CustomerInfo customerInfo)
    {
        return toAjax(customerInfoService.authCustomerInfo(customerInfo));
    }

    /**
     * 删除客户信息
     */
    @RequiresPermissions("system:client:remove")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerInfoService.deleteCustomerInfoByIds(ids));
    }
}
