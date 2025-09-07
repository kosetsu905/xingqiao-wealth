package com.xingqiao.system.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.system.api.domain.employee.AgencyEkyc;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CustomerSaleListResponse {
    private Long id;
    private Long userTempId;
    private String avatar;
    private String fullName;
    private String age;
    private String gender;
    private String phoneNumber;
    private String email;
    //投资金额
    private BigDecimal investmentAmount;
    //投资时间意向
    private String investmentTimeIntent;
    //感兴趣的产品
    List<AgencyEkyc.CustomerInvestmentPreference> investmentPreferences;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;




}