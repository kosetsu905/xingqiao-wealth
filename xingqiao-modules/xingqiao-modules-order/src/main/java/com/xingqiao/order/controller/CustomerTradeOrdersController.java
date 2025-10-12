package com.xingqiao.order.controller;

import java.util.List;
import java.io.IOException;
import java.math.BigDecimal;

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
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.order.domain.CustomerTradeOrders;
import com.xingqiao.order.service.ICustomerTradeOrdersService;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.utils.poi.ExcelUtil;
import com.xingqiao.common.core.web.page.TableDataInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 交易订单Controller
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
@Slf4j
@RestController
@RequestMapping("/order/trade")
public class CustomerTradeOrdersController extends BaseController
{
    @Autowired
    private ICustomerTradeOrdersService customerTradeOrdersService;

    /**
     * 查询交易订单列表
     */
    //@RequiresPermissions("order:trade:list")
    @GetMapping("/list")
    public TableDataInfo list(CustomerTradeOrders customerTradeOrders)
    {
        startPage();
        List<CustomerTradeOrders> list = customerTradeOrdersService.selectCustomerTradeOrdersList(customerTradeOrders);
        return getDataTable(list);
    }

    /**
     * 导出交易订单列表
     */
    //@RequiresPermissions("order:trade:export")
    @Log(title = "交易订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerTradeOrders customerTradeOrders) throws IOException
    {
        List<CustomerTradeOrders> list = customerTradeOrdersService.selectCustomerTradeOrdersList(customerTradeOrders);
        ExcelUtil<CustomerTradeOrders> util = new ExcelUtil<CustomerTradeOrders>(CustomerTradeOrders.class);
        util.exportExcel(response, list, "交易订单数据");
    }

    /**
     * 获取交易订单详细信息
     */
    //@RequiresPermissions("order:trade:query")
    @GetMapping(value = "{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(customerTradeOrdersService.selectCustomerTradeOrdersById(id));
    }

    /**
     * 新增交易订单
     */
    //@RequiresPermissions("order:trade:add")
    @Log(title = "交易订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerTradeOrders customerTradeOrders)
    {
        return toAjax(customerTradeOrdersService.insertCustomerTradeOrders(customerTradeOrders));
    }

    /**
     * 修改交易订单
     */
    //@RequiresPermissions("order:trade:edit")
    @Log(title = "交易订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerTradeOrders customerTradeOrders)
    {
        return toAjax(customerTradeOrdersService.updateCustomerTradeOrders(customerTradeOrders));
    }

    /**
     * 删除交易订单
     */
    //@RequiresPermissions("order:trade:remove")
    @Log(title = "交易订单", businessType = BusinessType.DELETE)
    @DeleteMapping("{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(customerTradeOrdersService.deleteCustomerTradeOrdersByIds(ids));
    }

    /**
     * 根据用户ID查询交易订单
     */
    @GetMapping("/user/{userId}")
    public TableDataInfo getByUserId(@PathVariable("userId") String userId)
    {
        List<CustomerTradeOrders> list = customerTradeOrdersService.selectCustomerTradeOrdersByUserId(userId);
        return getDataTable(list);
    }

    /**
     * 根据账户ID查询交易订单
     */
    @GetMapping("/account/{accountId}")
    public TableDataInfo getByAccountId(@PathVariable("accountId") String accountId)
    {
        List<CustomerTradeOrders> list = customerTradeOrdersService.selectCustomerTradeOrdersByAccountId(accountId);
        return getDataTable(list);
    }

    /**
     * 获取用户今日盈亏
     * 
     * @return 今日盈亏金额
     */
    @GetMapping("/today-profit-loss")
    public AjaxResult getTodayProfitLossByToken()
    {
        try {
            // 从token中获取当前登录用户ID
            Long userId = SecurityUtils.getUserId();
            if (userId == null || userId == 0) {
                return AjaxResult.error("用户未登录");
            }
            
            BigDecimal profitLoss = customerTradeOrdersService.getTodayProfitLossByUserId(userId.toString());
            return AjaxResult.success(profitLoss);
        } catch (Exception e) {
            log.error("获取今日盈亏异常", e);
            return AjaxResult.error("获取今日盈亏失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取用户今日盈亏（保留原有接口，向后兼容）
     * 
     * @param userId 用户ID
     * @return 今日盈亏金额
     */
    @GetMapping("/today-profit-loss/{userId}")
    public AjaxResult getTodayProfitLossByUserId(@PathVariable("userId") String userId)
    {
        try {
            BigDecimal profitLoss = customerTradeOrdersService.getTodayProfitLossByUserId(userId);
            return AjaxResult.success(profitLoss);
        } catch (Exception e) {
            return AjaxResult.error("获取今日盈亏失败：" + e.getMessage());
        }
    }
}