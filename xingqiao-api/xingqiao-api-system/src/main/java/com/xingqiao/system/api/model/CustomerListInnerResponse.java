package com.xingqiao.system.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerListInnerResponse {
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
}
