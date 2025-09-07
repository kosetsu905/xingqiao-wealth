package com.xingqiao.system.service.impl.employee;


import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.xingqiao.common.core.enums.TableIdEnums;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.system.api.domain.employee.AgencyEkyc;
import com.xingqiao.system.api.model.CustomerInfoResponse;
import com.xingqiao.system.api.model.CustomerListInnerResponse;
import com.xingqiao.system.api.model.CustomerListResponse;
import com.xingqiao.system.api.model.CustomerSaleListResponse;
import com.xingqiao.system.domain.SalesOpportunity;
import com.xingqiao.system.domain.employee.SysCustomerFinancial;
import com.xingqiao.system.domain.employee.SysCustomerInvestmentPreference;
import com.xingqiao.system.mapper.*;
import com.xingqiao.system.mapper.employee.CustomerExtMapper;
import com.xingqiao.system.mapper.employee.SysCustomerFinancialMapper;
import com.xingqiao.system.mapper.employee.SysCustomerInfoMapper;
import com.xingqiao.system.mapper.employee.SysCustomerInvestmentPreferenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.xingqiao.system.domain.employee.SysCustomerInfo;
import com.xingqiao.system.service.employee.ISysCustomerInfoService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 客户信息Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-23
 */

@Slf4j
@Service
public class SysCustomerInfoServiceImpl implements ISysCustomerInfoService
{
    @Resource
    private SysCustomerInfoMapper sysCustomerInfoMapper;

    @Resource
    private SysCustomerFinancialMapper customerFinancialMapper;

    @Resource
    private SysCustomerInvestmentPreferenceMapper investmentPreferenceMapper;
    @Resource
    private CustomerExtMapper customerExtMapper;
    @Resource
    private SalesOpportunityMapper salesOpportunityMapper;

    /**
     * 查询客户信息
     *
     * @param id 客户信息主键
     * @return 客户信息
     */
    @Override
    public SysCustomerInfo selectSysCustomerInfoById(Long id)
    {
        return sysCustomerInfoMapper.selectSysCustomerInfoById(id);
    }

    /**
     * 查询客户信息列表
     *
     * @param sysCustomerInfo 客户信息
     * @return 客户信息
     */
    @Override
    public List<SysCustomerInfo> selectSysCustomerInfoList(SysCustomerInfo sysCustomerInfo)
    {
        return sysCustomerInfoMapper.selectSysCustomerInfoList(sysCustomerInfo);
    }

    /**
     * 新增客户信息
     *
     * @param sysCustomerInfo 客户信息
     * @return 结果
     */
    @Override
    public int insertSysCustomerInfo(SysCustomerInfo sysCustomerInfo)
    {
        sysCustomerInfo.setCreateTime(DateUtils.getNowDate());
        return sysCustomerInfoMapper.insertSysCustomerInfo(sysCustomerInfo);
    }

    /**
     * 修改客户信息
     *
     * @param sysCustomerInfo 客户信息
     * @return 结果
     */
    @Override
    public int updateSysCustomerInfo(SysCustomerInfo sysCustomerInfo)
    {
        sysCustomerInfo.setUpdateTime(DateUtils.getNowDate());
        return sysCustomerInfoMapper.updateSysCustomerInfo(sysCustomerInfo);
    }

    /**
     * 批量删除客户信息
     *
     * @param ids 需要删除的客户信息主键
     * @return 结果
     */
    @Override
    public int deleteSysCustomerInfoByIds(Long[] ids)
    {
        return sysCustomerInfoMapper.deleteSysCustomerInfoByIds(ids);
    }

    /**
     * 删除客户信息信息
     *
     * @param id 客户信息主键
     * @return 结果
     */
    @Override
    public int deleteSysCustomerInfoById(Long id)
    {
        return sysCustomerInfoMapper.deleteSysCustomerInfoById(id);
    }

    @Override
    @Transactional
    public int saveCustomer(AgencyEkyc.CustomerSaveRequest request) {
        Snowflake snowflake = IdUtil.createSnowflake(TableIdEnums.SYS_CUSTOMER_INFO.getWorkerId(), TableIdEnums.SYS_CUSTOMER_INFO.getDatacenterId());
        // 生成唯一ID
        String tempUserId = snowflake.nextId()+"";
        // 保存客户基本信息
        SysCustomerInfo customerInfo = new SysCustomerInfo();
        customerInfo.setEmployeeId(request.getEmployeeId());
        customerInfo.setFullName(request.getCustomerInfo().getFullName());
        customerInfo.setAge(request.getCustomerInfo().getAge());
        customerInfo.setGender(request.getCustomerInfo().getGender());
        customerInfo.setPhoneNumber(request.getCustomerInfo().getPhoneNumber());
        customerInfo.setEmail(request.getCustomerInfo().getEmail());
        customerInfo.setMaritalStatus(request.getCustomerInfo().getMaritalStatus());
        customerInfo.setChildCount(request.getCustomerInfo().getChildCount());
        customerInfo.setAddress(request.getCustomerInfo().getAddress());
        customerInfo.setRemark(request.getCustomerInfo().getRemark());
        customerInfo.setUserTempId(tempUserId);
        customerInfo.setStatus("0"); // 正常状态
        customerInfo.setCreateBy(SecurityUtils.getUsername());
        customerInfo.setUpdateBy(SecurityUtils.getUsername());
        customerInfo.setCreateTime(DateUtils.getNowDate());
        customerInfo.setUpdateTime(DateUtils.getNowDate());
        sysCustomerInfoMapper.insertSysCustomerInfo(customerInfo);

        // 保存客户财务信息
        SysCustomerFinancial financial = new SysCustomerFinancial();
        financial.setUserTempId(tempUserId);
        financial.setFamilyTotalAsset(request.getCustomerFinancial().getFamilyTotalAsset());
        financial.setFamilyDebt(request.getCustomerFinancial().getFamilyDebt());
        financial.setFamilyAnnualIncome(request.getCustomerFinancial().getFamilyAnnualIncome());
        financial.setNewInvestmentAmount(request.getCustomerFinancial().getNewInvestmentAmount());
        financial.setStatus("0");
        financial.setCreateBy(SecurityUtils.getUsername());
        financial.setUpdateBy(SecurityUtils.getUsername());
        financial.setCreateTime(DateUtils.getNowDate());
        financial.setUpdateTime(DateUtils.getNowDate());
        customerFinancialMapper.insertSysCustomerFinancial(financial);


        // 保存投资偏好
        List<AgencyEkyc.CustomerInvestmentPreference> requestReferences = request.getInvestmentPreferences();
        for (AgencyEkyc.CustomerInvestmentPreference requestReference : requestReferences){
            SysCustomerInvestmentPreference investmentPreference = new SysCustomerInvestmentPreference();
            investmentPreference.setUserTempId(tempUserId);
            investmentPreference.setProductType(requestReference.getProductType());
            investmentPreference.setStatus("0");
            investmentPreference.setCreateBy(SecurityUtils.getUsername());
            investmentPreference.setUpdateBy(SecurityUtils.getUsername());
            investmentPreference.setCreateTime(DateUtils.getNowDate());
            investmentPreference.setUpdateTime(DateUtils.getNowDate());
            int insert =investmentPreferenceMapper.insertSysCustomerInvestmentPreference(investmentPreference);
            log.info("保存投资偏好：{}",insert);
        }
        return 1;
    }


    @Override
    public CustomerInfoResponse getCustomerInfo(Long employeeId, Long id) {
        // 组装返回数据
        CustomerInfoResponse response = new CustomerInfoResponse();
        // 查询客户基本信息
        SysCustomerInfo sysCustomerInfo= sysCustomerInfoMapper.selectSysCustomerInfoById(id);
        if (sysCustomerInfo == null){
            log.info("客户信息不存在");
            return response;
        }

        AgencyEkyc.CustomerExtInfo customerInfo = new AgencyEkyc.CustomerExtInfo();
        BeanUtils.copyProperties(sysCustomerInfo,customerInfo);
        response.setCustomerInfo(customerInfo);

        String userTempId=sysCustomerInfo.getUserTempId();
        // 查询客户财务信息
        SysCustomerFinancial queryCustomerFinancial=new SysCustomerFinancial();
        queryCustomerFinancial.setUserTempId(userTempId);
        List<SysCustomerFinancial>  financialList = customerFinancialMapper.selectSysCustomerFinancialList(queryCustomerFinancial);

        // 查询投资偏好
        SysCustomerInvestmentPreference queryInvestmentPreference=new SysCustomerInvestmentPreference() ;
        queryInvestmentPreference.setUserTempId(userTempId);
        List<SysCustomerInvestmentPreference> preferences = investmentPreferenceMapper.selectSysCustomerInvestmentPreferenceList(queryInvestmentPreference);



        if (CollectionUtils.isNotEmpty(financialList)){
            SysCustomerFinancial sysCustomerFinancial = financialList.get(0);
            AgencyEkyc.CustomerFinancial customerFinancial = new AgencyEkyc.CustomerFinancial();
            BeanUtils.copyProperties(sysCustomerFinancial,customerFinancial);
            response.setCustomerFinancial(customerFinancial);
        }
        if (CollectionUtils.isNotEmpty(preferences)){
            List<AgencyEkyc.CustomerInvestmentPreference> customerInvestmentPreferences = preferences.stream().map(item -> {
                AgencyEkyc.CustomerInvestmentPreference preference = new AgencyEkyc.CustomerInvestmentPreference();
                BeanUtils.copyProperties(item, preference);
                return preference;
            }).collect(Collectors.toList());
            response.setInvestmentPreferences(customerInvestmentPreferences);
        }
        return response;
    }

    @Override
    public List<CustomerListResponse> getList(AgencyEkyc.CustomerQueryRequest request) {

        // 查询客户基本信息
        SysCustomerInfo queryCustomerInfo=new SysCustomerInfo();
        BeanUtils.copyProperties(request,queryCustomerInfo);
        List<SysCustomerInfo>  customerList = sysCustomerInfoMapper.selectSysCustomerInfoList(queryCustomerInfo);
        // 2. 收集所有的 user_temp_id
        Set<String> userTempIds = customerList.stream()
                .map(SysCustomerInfo::getUserTempId)
                .collect(Collectors.toSet());

        // 3. 批量查询这些 user_temp_id 对应的投资偏好（可按需实现批量查询，或循环单个查）
        Map<String, List<AgencyEkyc.CustomerInvestmentPreference>> preferencesMap = new HashMap<>();
        for (String userTempId : userTempIds) {
            SysCustomerInvestmentPreference request3=new SysCustomerInvestmentPreference();
            request3.setUserTempId(userTempId);
            List<SysCustomerInvestmentPreference> prefs = investmentPreferenceMapper
                    .selectSysCustomerInvestmentPreferenceList(request3);
            //转List<CustomerInvestmentPreference>
            List<AgencyEkyc.CustomerInvestmentPreference> customerInvestmentPreferences =
                    prefs.stream().map(item -> {
                        AgencyEkyc.CustomerInvestmentPreference preference = new AgencyEkyc.CustomerInvestmentPreference();
                        BeanUtils.copyProperties(item, preference);
                        return preference;
                    }).collect(Collectors.toList());
            preferencesMap.put(userTempId, customerInvestmentPreferences);
        }

        // 4. 组装最终返回的 VO 列表
        return customerList.stream().map(base -> {
            CustomerListResponse vo = new CustomerListResponse();
            BeanUtils.copyProperties(base, vo);
            // 设置投资偏好列表
            vo.setInvestmentPreferences(preferencesMap.getOrDefault(base.getUserTempId(), Collections.emptyList()));
            return vo;
        }).collect(Collectors.toList());


    }

    @Override
    public List<CustomerSaleListResponse> getCustomerSalesList(AgencyEkyc.CustomerQueryRequest request) {
        // 1. 查询销售机会基础列表
        SalesOpportunity request2=new SalesOpportunity();
        request2.setEmployeeId(request.getEmployeeId());
        BeanUtils.copyProperties(request,request2);
        List<SalesOpportunity> baseList=salesOpportunityMapper.selectSalesOpportunityList(request2);
        // 2. 收集所有的 user_temp_id
        Set<String> userTempIds = baseList.stream()
                .map(SalesOpportunity::getUserTempId)
                .collect(Collectors.toSet());

        // 3. 批量查询这些 user_temp_id 对应的投资偏好（可按需实现批量查询，或循环单个查）
        Map<String, List<AgencyEkyc.CustomerInvestmentPreference>> preferencesMap = new HashMap<>();
        for (String userTempId : userTempIds) {
            SysCustomerInvestmentPreference request3=new SysCustomerInvestmentPreference();
            request3.setUserTempId(userTempId);
            List<SysCustomerInvestmentPreference> prefs = investmentPreferenceMapper
                    .selectSysCustomerInvestmentPreferenceList(request3);
            //转List<CustomerInvestmentPreference>
            List<AgencyEkyc.CustomerInvestmentPreference> customerInvestmentPreferences =
                    prefs.stream().map(item -> {
                        AgencyEkyc.CustomerInvestmentPreference preference = new AgencyEkyc.CustomerInvestmentPreference();
                        BeanUtils.copyProperties(item, preference);
                        return preference;
                    }).collect(Collectors.toList());
            preferencesMap.put(userTempId, customerInvestmentPreferences);
        }

        // 4. 组装最终返回的 VO 列表
        return baseList.stream().map(base -> {
            CustomerSaleListResponse vo = new CustomerSaleListResponse();
            BeanUtils.copyProperties(base, vo);
            // 设置投资偏好列表
            vo.setInvestmentPreferences(preferencesMap.getOrDefault(base.getUserTempId(), Collections.emptyList()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public int saveCustomerIntention(AgencyEkyc.CustomerSaleSaveRequest request) {
        Snowflake snowflake = IdUtil.createSnowflake(TableIdEnums.SALES_OPPORTUNITY.getWorkerId(), TableIdEnums.SALES_OPPORTUNITY.getDatacenterId());
        // 生成唯一ID
        String tempUserId = snowflake.nextId()+"";
        SalesOpportunity request2=new SalesOpportunity();
        BeanUtils.copyProperties(request,request2);
        request2.setInterestedProducts(null);
        request2.setUserTempId(tempUserId);
        request2.setCreateTime(DateUtils.getNowDate());
        request2.setUpdateTime(DateUtils.getNowDate());
        request2.setCreateBy(SecurityUtils.getUsername());
        request2.setUpdateBy(SecurityUtils.getUsername());
        int insert =salesOpportunityMapper.insertSalesOpportunity( request2);
        if (insert>0){
            // 批量插入投资偏好
            List<SysCustomerInvestmentPreference> preferenceList = request.getInterestedProducts().stream().map(item -> {
                SysCustomerInvestmentPreference preference = new SysCustomerInvestmentPreference();
                preference.setUserTempId(tempUserId);
                preference.setProductType(item.getProductType());
                return preference;
            }).collect(Collectors.toList());
            for(SysCustomerInvestmentPreference preference:preferenceList){
                int insert2 = investmentPreferenceMapper.insertSysCustomerInvestmentPreference(preference);
                log.info("批量插入投资偏好：{}",insert2);
            }
        }
        return 1;
    }



    @Transactional
    @Override
    public int deleteCustomerInfo(String userTempId, Long employeeId) {
        //删除客户信息
        customerExtMapper.deleteCustomerInfo(userTempId);
        //删除投资偏好
        customerExtMapper.deleteInvestmentPreferenceByUserId(userTempId);
        //删除客户财务信息
        customerExtMapper.deleteCustomerFinancialByUserId(userTempId);
        return 1;
    }

    @Override
    public List<CustomerListInnerResponse> getCustomerInnerList(AgencyEkyc.CustomerQueryInnerRequest request) {
       if (CollectionUtils.isEmpty(request.getIdList())) {
           return Collections.emptyList();
       }
       return customerExtMapper.selectSysCustomerInfoByIds(request.getIdList());
    }


    @Transactional
    @Override
    public int salesDelete(Long id, Long employeeId) {
        SalesOpportunity dbSalesOpportunity =  salesOpportunityMapper.selectSalesOpportunityById(id);
        String userTempId= dbSalesOpportunity.getUserTempId();
        //删除销售机会
        salesOpportunityMapper.deleteSalesOpportunityById(id);
        //删除投资偏好
        customerExtMapper.deleteInvestmentPreferenceByUserId(userTempId);
        return 1;
    }

    @Override
    public CustomerSaleListResponse getSalesOpportunityDetail(Long id) {
        CustomerSaleListResponse response = new CustomerSaleListResponse();
        SalesOpportunity dbSalesOpportunity =  salesOpportunityMapper.selectSalesOpportunityById(id);
        BeanUtils.copyProperties(dbSalesOpportunity,response);
        SysCustomerInvestmentPreference queryDto=new SysCustomerInvestmentPreference();
        queryDto.setUserTempId(dbSalesOpportunity.getUserTempId());
        List<SysCustomerInvestmentPreference> preferences = investmentPreferenceMapper
                .selectSysCustomerInvestmentPreferenceList(queryDto);
        if (CollectionUtils.isNotEmpty(preferences)){
            List<AgencyEkyc.CustomerInvestmentPreference> customerInvestmentPreferences = preferences.stream().map(item -> {
                AgencyEkyc.CustomerInvestmentPreference preference = new AgencyEkyc.CustomerInvestmentPreference();
                BeanUtils.copyProperties(item, preference);
                return preference;
            }).collect(Collectors.toList());
            response.setInvestmentPreferences(customerInvestmentPreferences);
        }
        return response;
    }

    @Transactional
    @Override
    public int updateCustomerIntention(AgencyEkyc.CustomerSaleSaveRequest request) {
        SalesOpportunity dbSalesOpportunity =  salesOpportunityMapper.selectSalesOpportunityById(request.getId());
        String userTempId= dbSalesOpportunity.getUserTempId();
        SalesOpportunity request2=new SalesOpportunity();
        request2.setId(dbSalesOpportunity.getId());
        BeanUtils.copyProperties(request,request2);
        request2.setInterestedProducts(null);
        request2.setUserTempId(userTempId);
        request2.setUpdateTime(DateUtils.getNowDate());
        request2.setUpdateBy(SecurityUtils.getUsername());
        int insert =salesOpportunityMapper.updateSalesOpportunity(request2);
        log.info("更新销售机会：{}",insert);
        //删除投资偏好
        customerExtMapper.deleteInvestmentPreferenceByUserId(userTempId);
        // 批量插入投资偏好
        List<SysCustomerInvestmentPreference> preferenceList = request.getInterestedProducts().stream().map(item -> {
            SysCustomerInvestmentPreference preference = new SysCustomerInvestmentPreference();
            preference.setUserTempId(userTempId);
            preference.setProductType(item.getProductType());
            return preference;
        }).collect(Collectors.toList());
        for(SysCustomerInvestmentPreference preference:preferenceList){
            int insert2 = investmentPreferenceMapper.insertSysCustomerInvestmentPreference(preference);
            log.info("批量插入投资偏好：{}",insert2);
        }

        return 1;
    }

}