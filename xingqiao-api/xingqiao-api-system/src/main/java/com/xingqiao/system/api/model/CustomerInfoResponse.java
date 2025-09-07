package com.xingqiao.system.api.model;

import com.xingqiao.system.api.domain.employee.AgencyEkyc;
import lombok.Data;

import java.util.List;

@Data
public class CustomerInfoResponse {
    // 客户信息
    private AgencyEkyc.CustomerExtInfo customerInfo;
    // 客户财务信息
    private AgencyEkyc.CustomerFinancial customerFinancial;
    //投资偏好
    List<AgencyEkyc.CustomerInvestmentPreference> investmentPreferences;
}
