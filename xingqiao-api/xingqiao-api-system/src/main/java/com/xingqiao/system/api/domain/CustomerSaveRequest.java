package com.xingqiao.system.api.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CustomerSaveRequest {
    private Long employeeId;

    private Long customerId;
    // 客户信息
    private CustomerInfo customerInfo;
    // 客户财务信息
    private CustomerFinancial customerFinancial;
    //投资偏好
    List<CustomerInvestmentPreference> investmentPreferences;

}

