package com.xingqiao.system.api.enums;

public enum MaritalStatus {
    SINGLE("未婚"),
    MARRIED("已婚"),
    DIVORCED("离异"),
    WIDOWED("丧偶"),
    OTHER("其他");

    private final String description;

    MaritalStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
