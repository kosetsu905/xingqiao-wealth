package com.xingqiao.system.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.system.api.domain.CustomerInvestmentPreference;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CustomerListResponse {
    private Long id;
    private String userTempId;
    private String avatar;
    private String fullName;
    private String age;
    private String gender;
    private String phoneNumber;
    private String email;
    private String maritalStatus;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //已经购买产品
    List<CustomerInvestmentPreference> buyedInvestmentList;
    //感兴趣的产品
    List<CustomerInvestmentPreference> investmentPreferences;


}
