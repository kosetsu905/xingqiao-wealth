package com.xingqiao.system.api.enums;

import lombok.Getter;

@Getter
public enum AuditStatusEnums {
    NO(0, "未提交审核"),
    INIT(1, "待审核"),
    ON(2, "审核通过"),
    FAIL(-1, "审核失败"),
    ;

    // getter 方法
    private final Integer code; // 编码（对应JSON中的值）
    private final String name; // 名称

    AuditStatusEnums(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

}