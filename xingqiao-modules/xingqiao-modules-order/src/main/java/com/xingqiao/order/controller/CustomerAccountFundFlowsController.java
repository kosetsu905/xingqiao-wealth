package com.xingqiao.order.controller;

import java.util.List;
import java.io.IOException;
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
import com.xingqiao.order.domain.CustomerAccountFundFlows;
import com.xingqiao.order.service.trade.ICustomerAccountFundFlowsService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;

/**
 * 账户资金流水Controller
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
@RestController
@RequestMapping("/order/fund")
public class CustomerAccountFundFlowsController extends BaseController
{
    @Autowired
    private ICustomerAccountFundFlowsService customerAccountFundFlowsService;

    /**
     * 查询账户资金流水列表
     */
    //@RequiresPermissions("order:fund:list")
    @GetMapping("/list")
    public TableDataInfo list(CustomerAccountFundFlows customerAccountFundFlows)
    {
        startPage();
        List<CustomerAccountFundFlows> list = customerAccountFundFlowsService.selectCustomerAccountFundFlowsList(customerAccountFundFlows);
        return getDataTable(list);
    }

    /**
     * 导出账户资金流水列表
     */
    //@RequiresPermissions("order:fund:export")
    @Log(title = "账户资金流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerAccountFundFlows customerAccountFundFlows) throws IOException
    {
        List<CustomerAccountFundFlows> list = customerAccountFundFlowsService.selectCustomerAccountFundFlowsList(customerAccountFundFlows);
        ExcelUtil<CustomerAccountFundFlows> util = new ExcelUtil<CustomerAccountFundFlows>(CustomerAccountFundFlows.class);
        util.exportExcel(response, list, "账户资金流水数据");
    }

    /**
     * 获取账户资金流水详细信息
     */
    //@RequiresPermissions("order:fund:query")
    @GetMapping(value = "{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(customerAccountFundFlowsService.selectCustomerAccountFundFlowsById(id));
    }

    /**
     * 新增账户资金流水
     */
    //@RequiresPermissions("order:fund:add")
    @Log(title = "账户资金流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerAccountFundFlows customerAccountFundFlows)
    {
        return toAjax(customerAccountFundFlowsService.insertCustomerAccountFundFlows(customerAccountFundFlows));
    }

    /**
     * 修改账户资金流水
     */
    //@RequiresPermissions("order:fund:edit")
    @Log(title = "账户资金流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerAccountFundFlows customerAccountFundFlows)
    {
        return toAjax(customerAccountFundFlowsService.updateCustomerAccountFundFlows(customerAccountFundFlows));
    }

    /**
     * 删除账户资金流水
     */
    //@RequiresPermissions("order:fund:remove")
    @Log(title = "账户资金流水", businessType = BusinessType.DELETE)
    @DeleteMapping("{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(customerAccountFundFlowsService.deleteCustomerAccountFundFlowsByIds(ids));
    }

    /**
     * 根据用户ID查询账户资金流水
     */
    @GetMapping("/user/{userId}")
    public TableDataInfo getByUserId(@PathVariable("userId") String userId)
    {
        List<CustomerAccountFundFlows> list = customerAccountFundFlowsService.selectCustomerAccountFundFlowsByUserId(userId);
        return getDataTable(list);
    }

    /**
     * 根据账户ID查询账户资金流水
     */
    @GetMapping("/account/{accountId}")
    public TableDataInfo getByAccountId(@PathVariable("accountId") String accountId)
    {
        List<CustomerAccountFundFlows> list = customerAccountFundFlowsService.selectCustomerAccountFundFlowsByAccountId(accountId);
        return getDataTable(list);
    }
}