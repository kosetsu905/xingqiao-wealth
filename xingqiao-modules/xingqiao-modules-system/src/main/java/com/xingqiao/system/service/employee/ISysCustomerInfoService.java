package com.xingqiao.system.service.employee;

import java.util.List;

import com.xingqiao.system.api.domain.employee.AgencyEkyc;
import com.xingqiao.system.api.model.CustomerInfoResponse;
import com.xingqiao.system.api.model.CustomerListInnerResponse;
import com.xingqiao.system.api.model.CustomerListResponse;
import com.xingqiao.system.api.model.CustomerSaleListResponse;
import com.xingqiao.system.domain.employee.SysCustomerInfo;

/**
 * 客户信息Service接口
 *
 * @author xingqiao
 * @date 2025-08-23
 */
public interface ISysCustomerInfoService
{
    /**
     * 查询客户信息
     *
     * @param id 客户信息主键
     * @return 客户信息
     */
    public SysCustomerInfo selectSysCustomerInfoById(Long id);

    /**
     * 查询客户信息列表
     *
     * @param sysCustomerInfo 客户信息
     * @return 客户信息集合
     */
    public List<SysCustomerInfo> selectSysCustomerInfoList(SysCustomerInfo sysCustomerInfo);

    /**
     * 新增客户信息
     *
     * @param sysCustomerInfo 客户信息
     * @return 结果
     */
    public int insertSysCustomerInfo(SysCustomerInfo sysCustomerInfo);

    /**
     * 修改客户信息
     *
     * @param sysCustomerInfo 客户信息
     * @return 结果
     */
    public int updateSysCustomerInfo(SysCustomerInfo sysCustomerInfo);

    /**
     * 批量删除客户信息
     *
     * @param ids 需要删除的客户信息主键集合
     * @return 结果
     */
    public int deleteSysCustomerInfoByIds(Long[] ids);

    /**
     * 删除客户信息信息
     *
     * @param id 客户信息主键
     * @return 结果
     */
    public int deleteSysCustomerInfoById(Long id);

    int saveCustomer(AgencyEkyc.CustomerSaveRequest request);

    CustomerInfoResponse getCustomerInfo(Long employeeId,Long id);

    List<CustomerListResponse> getList(AgencyEkyc.CustomerQueryRequest request);

    List<CustomerSaleListResponse> getCustomerSalesList(AgencyEkyc.CustomerQueryRequest request);

    int saveCustomerIntention(AgencyEkyc.CustomerSaleSaveRequest request);

    int salesDelete(Long id, Long employeeId);

    CustomerSaleListResponse getSalesOpportunityDetail(Long id);

    int updateCustomerIntention(AgencyEkyc.CustomerSaleSaveRequest request);

    int deleteCustomerInfo(String userTempId, Long employeeId);

    List<CustomerListInnerResponse> getCustomerInnerList(AgencyEkyc.CustomerQueryInnerRequest request);
}