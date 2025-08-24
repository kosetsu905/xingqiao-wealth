package com.xingqiao.system.api.enums;

import lombok.Getter;

@Getter
public enum IdType {
    PASSPORT("passport", "护照"),
    ID_CARD("id_card", "身份证"),
    DRIVER_LICENSE("driver_license", "驾驶证"),
    OTHER("other", "其他");

    // getter 方法
    private final String code; // 编码（对应JSON中的值）
    private final String name; // 名称

    IdType(String code, String name) {
        this.code = code;
        this.name = name;
    }

}