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
import com.xingqiao.order.domain.CustomerPositions;
import com.xingqiao.order.service.trade.ICustomerPositionsService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 客户持仓Controller
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
@RestController
@RequestMapping("/client/position")
public class CustomerPositionsController extends BaseController
{
    @Autowired
    private ICustomerPositionsService customerPositionsService;

    /**
     * 查询客户持仓列表
     */
    //@RequiresPermissions("order:position:list")
    @GetMapping("/list")
    public TableDataInfo list(CustomerPositions customerPositions)
    {
        startPage();
        List<CustomerPositions> list = customerPositionsService.selectCustomerPositionsList(customerPositions);
        return getDataTable(list);
    }

    /**
     * 导出客户持仓列表
     */
    //@RequiresPermissions("order:position:export")
    @Log(title = "客户持仓", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerPositions customerPositions)
    {
        List<CustomerPositions> list = customerPositionsService.selectCustomerPositionsList(customerPositions);
        ExcelUtil<CustomerPositions> util = new ExcelUtil<CustomerPositions>(CustomerPositions.class);
        util.exportExcel(response, list, "客户持仓数据");
    }

    /**
     * 获取客户持仓详细信息
     */
    //@RequiresPermissions("order:position:query")
    @GetMapping(value = "{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(customerPositionsService.selectCustomerPositionsById(id));
    }

    /**
     * 新增客户持仓
     */
    //@RequiresPermissions("order:position:add")
    @Log(title = "客户持仓", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerPositions customerPositions)
    {
        return toAjax(customerPositionsService.insertCustomerPositions(customerPositions));
    }

    /**
     * 修改客户持仓
     */
    //@RequiresPermissions("order:position:edit")
    @Log(title = "客户持仓", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerPositions customerPositions)
    {
        return toAjax(customerPositionsService.updateCustomerPositions(customerPositions));
    }

    /**
     * 删除客户持仓
     */
    //@RequiresPermissions("order:position:remove")
    @Log(title = "客户持仓", businessType = BusinessType.DELETE)
    @DeleteMapping("{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(customerPositionsService.deleteCustomerPositionsByIds(ids));
    }

    /**
     * 根据用户ID查询客户持仓
     */
    @GetMapping("/user/{userId}")
    public TableDataInfo getByUserId(@PathVariable("userId") String userId)
    {
        List<CustomerPositions> list = customerPositionsService.selectCustomerPositionsByUserId(userId);
        return getDataTable(list);
    }

    /**
     * 根据账户ID查询客户持仓
     */
    @GetMapping("/account/{accountId}")
    public TableDataInfo getByAccountId(@PathVariable("accountId") String accountId)
    {
        List<CustomerPositions> list = customerPositionsService.selectCustomerPositionsByAccountId(accountId);
        return getDataTable(list);
    }
}