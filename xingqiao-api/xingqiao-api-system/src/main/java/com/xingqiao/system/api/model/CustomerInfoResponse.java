package com.xingqiao.system.api.model;

import com.xingqiao.system.api.domain.CustomerFinancial;
import com.xingqiao.system.api.domain.CustomerInfo;
import com.xingqiao.system.api.domain.CustomerInvestmentPreference;
import lombok.Data;

import java.util.List;

@Data
public class CustomerInfoResponse {
    // 客户信息
    private CustomerInfo customerInfo;
    // 客户财务信息
    private CustomerFinancial customerFinancial;
    //投资偏好
    List<CustomerInvestmentPreference> investmentPreferences;
}
