package com.xingqiao.order.controller;

import com.xingqiao.common.core.text.Convert;
import com.xingqiao.common.core.web.domain.AjaxResult;
import com.xingqiao.common.core.web.page.TableDataInfo;
import com.xingqiao.order.service.ICustomerAccountsService;
import com.xingqiao.order.service.impl.CustomerAccountsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.order.domain.CustomerAccounts;
import com.xingqiao.common.security.annotation.RequiresPermissions;

import java.util.Arrays;
import java.util.List;
import com.xingqiao.common.log.enums.BusinessType;
import com.xingqiao.common.log.annotation.Log;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/client/account")
public class CustomerAccountsController extends BaseController {

    @Autowired
    private ICustomerAccountsService customerAccountsService;

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    /**
     * 查询账户列表（分页）
     */
    //@RequiresPermissions("system:account:list")
    @GetMapping("/list")
    public TableDataInfo list(CustomerAccounts customerAccounts) {
        startPage();
        List<CustomerAccounts> list = customerAccountsService.selectCustomerAccountsList(customerAccounts);
        return getDataTable(list);
    }

    /**
     * 获取账户详情
     */
    //@RequiresPermissions("system:account:query")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(customerAccountsService.selectCustomerAccountsById(id));
    }

    /**
     * 根据用户ID查询账户
     */
    @GetMapping("/user/{userId}")
    public AjaxResult getByUser(@PathVariable Long userId) {
        CustomerAccounts account = customerAccountsService.selectCustomerAccountsByUserId(userId);
        return account != null ? success(account) : error("账户不存在");
    }

    /**
     * 创建新账户
     */
    //@RequiresPermissions("system:account:add")
    @Log(title = "资金账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult create(
            @RequestParam Long userId,
            @RequestParam String currency,
            @RequestParam(required = false) String remark) {
        try {
            int rows = customerAccountsService.createAccount(userId, currency, remark);
            return rows > 0 ? success("创建成功") : error("创建失败");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 修改账户（带乐观锁）
     */
    //@RequiresPermissions("system:account:edit")
    @Log(title = "资金账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerAccounts customerAccounts) {
        return toAjax(customerAccountsService.updateCustomerAccounts(customerAccounts));
    }

    /**
     * 冻结账户
     */
    //@RequiresPermissions("system:account:edit")
    @Log(title = "资金账户", businessType = BusinessType.UPDATE)
    @PostMapping("/freeze/{id}")
    public AjaxResult freeze(@PathVariable Long id) {
        int rows = customerAccountsService.freezeAccount(id);
        return rows > 0 ? success("冻结成功") : error("冻结失败");
    }

    /**
     * 解冻账户
     */
    //@RequiresPermissions("system:account:edit")
    @Log(title = "资金账户", businessType = BusinessType.UPDATE)
    @PostMapping("/unfreeze/{id}")
    public AjaxResult unfreeze(@PathVariable Long id) {
        int rows = customerAccountsService.unfreezeAccount(id);
        return rows > 0 ? success("解冻成功") : error("解冻失败");
    }

    /**
     * 销户
     */
    //@RequiresPermissions("system:account:remove")
    @Log(title = "资金账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/close/{id}")
    public AjaxResult close(@PathVariable Long id) {
        int rows = customerAccountsService.closeAccount(id);
        return rows > 0 ? success("销户成功") : error("销户失败");
    }

    /**
     * 批量删除账户
     */
    //@RequiresPermissions("system:account:remove")
    @Log(title = "资金账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(customerAccountsService.deleteCustomerAccountsByIds(ids));
    }
}
