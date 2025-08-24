package com.xingqiao.system.api.domain;

import lombok.Data;

@Data
public class CustomerQueryRequest {

    private String phoneNumber;
    private String email;
    private Integer pageNum;
    private Integer pageSize;
    private Long employeeId;
}
