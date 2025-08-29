package com.xingqiao.system.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class CustomerQueryInnerRequest {

    private List<Long> idList;
}
