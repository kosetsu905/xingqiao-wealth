package com.xingqiao.system.controller;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.core.web.page.TableDataInfo;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.domain.CustomerQueryInnerRequest;
import com.xingqiao.system.api.domain.CustomerQueryRequest;
import com.xingqiao.system.api.domain.CustomerSaleSaveRequest;
import com.xingqiao.system.api.domain.CustomerSaveRequest;
import com.xingqiao.system.api.model.CustomerInfoResponse;
import com.xingqiao.system.api.model.CustomerListInnerResponse;
import com.xingqiao.system.api.model.CustomerListResponse;
import com.xingqiao.system.api.model.CustomerSaleListResponse;
import com.xingqiao.system.service.ISysCustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agency/customer")
public class AgencyCustomerController extends BaseController {

    @Autowired
    private ISysCustomerInfoService customerService;

    /**
     * 保存客户信息
     */
    @PostMapping("/save")
    public R saveCustomer(@RequestBody CustomerSaveRequest request) {
        try {
            Long userId = SecurityUtils.getUserId();
            request.setEmployeeId(userId);
            customerService.saveCustomer(request);
            return R.ok("保存成功");
        } catch (Exception e) {
            logger.error("保存客户信息失败：", e);
            return R.fail("失败");
        }
    }

    /**
     * 获取客户信息
     */
    @GetMapping("/info")
    public R getCustomerInfo(Long id) {
        try {
            Long employeeId = SecurityUtils.getUserId();
            CustomerInfoResponse response = customerService.getCustomerInfo(employeeId,id);
            return R.ok(response);
        } catch (Exception e) {
            logger.error("获取客户信息失败：", e);
            return R.fail("失败");
        }
    }



    /**
     * 获取客户信息
     */
    @GetMapping("/list")
    public TableDataInfo getList(CustomerQueryRequest request) {
        Long employeeId = SecurityUtils.getUserId();
        request.setEmployeeId(employeeId);
        startPage();
        List<CustomerListResponse> list = customerService.getList(request);
        return getDataTable(list);

    }

    /**
     * 删除销售机会
     */
    @GetMapping("/deleteCustomerInfo")
    public R deleteCustomerInfo(String userTempId) {
        Long employeeId = SecurityUtils.getUserId();
        return R.ok(customerService.deleteCustomerInfo(userTempId, employeeId));

    }

     /**
     * 删除销售机会
     */
    @GetMapping("/salesDelete")
    public R salesDelete(Long id) {
        Long employeeId = SecurityUtils.getUserId();
        return R.ok(customerService.salesDelete(id, employeeId));

    }


    /**
     * 获取销售机会列表
     */
    @GetMapping("/salesList")
    public TableDataInfo getCustomerSalesList(CustomerQueryRequest request) {
        Long employeeId = SecurityUtils.getUserId();
        request.setEmployeeId(employeeId);
        startPage();
        List<CustomerSaleListResponse> list = customerService.getCustomerSalesList(request);
        return getDataTable(list);

    }

    /**
     * 获取销售机会详情
     */
    @GetMapping("/getSalesOpportunityDetail")
    public R getSalesOpportunityDetail(Long id) {
        return R.ok(customerService.getSalesOpportunityDetail(id));
    }


    /**
     * 保存销售机会
     */
    @PostMapping("/saveIntention")
    public R saveCustomerIntention(@RequestBody CustomerSaleSaveRequest request) {
        try {
            Long userId = SecurityUtils.getUserId();
            request.setEmployeeId(userId);
            customerService.saveCustomerIntention(request);
            return R.ok("保存成功");
        } catch (Exception e) {
            logger.error("保存客户信息失败：", e);
            return R.fail("失败");
        }
    }

    /**
     * 更新销售机会
     */
    @PostMapping("/updateIntention")
    public R updateCustomerIntention(@RequestBody CustomerSaleSaveRequest request) {
        try {
            Long userId = SecurityUtils.getUserId();
            request.setEmployeeId(userId);
            customerService.updateCustomerIntention(request);
            return R.ok("更新成功");
        } catch (Exception e) {
            logger.error("更新客户信息失败：", e);
            return R.fail("失败");
        }
    }


    /**
     * 获取客户信息
     */
    @PostMapping("/customerList")
    public R<List<CustomerListInnerResponse>> getCustomerInnerList(@RequestBody CustomerQueryInnerRequest request) {
        return R.ok(customerService.getCustomerInnerList(request));
    }

}
