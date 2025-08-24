package com.xingqiao.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
public class CustomerInfo{
    private Long id;
    private Long userId;
    private String userTempId;
    private String fullName;
    private String age;
    private String gender;
    private String phoneNumber;
    private String email;
    private String maritalStatus;
    private Integer childCount;
    private String address;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
