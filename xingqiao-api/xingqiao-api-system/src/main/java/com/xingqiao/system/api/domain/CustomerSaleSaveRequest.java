package com.xingqiao.system.api.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
public class CustomerSaleSaveRequest {

    private Long id;

    private String userTempId;

    private Long employeeId;

    private Long customerId;

    private String fullName;

    private String phoneNumber;

    private String email;

    private String remark;

    private BigDecimal investmentAmount;


    private String investmentTimeIntent;
    //投资偏好
    List<CustomerInvestmentPreference> interestedProducts;

}

