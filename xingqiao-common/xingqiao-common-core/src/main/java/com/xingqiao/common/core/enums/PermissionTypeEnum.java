package com.xingqiao.common.core.enums;


/**
 * 权限类型枚举
 */
public enum PermissionTypeEnum {

    MARKET("MARKET", "市场权限"),
    BOARD("BOARD", "板块权限"),
    PRODUCT("PRODUCT", "产品权限"),
    TRADE("TRADE", "交易权限"),
    SECURITY("SECURITY", "证券类型权限");

    private final String code;
    private final String description;

    PermissionTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PermissionTypeEnum getByCode(String code) {
        for (PermissionTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
