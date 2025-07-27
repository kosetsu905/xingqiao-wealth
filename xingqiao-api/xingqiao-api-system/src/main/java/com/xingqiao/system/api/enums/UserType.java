package com.xingqiao.system.api.enums;


import lombok.Data;

//客户02 ，业务员01
public enum UserType {
    PLATFORM("00", "平台"),
    CUSTOMER("01", "客户"),
    SALESMAN("02", "业务员");
    private String code;
    private String message;
    UserType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
