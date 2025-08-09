package com.xingqiao.common.core.enums;

public enum RegistrationStep {
    PHONE_VERIFICATION_CODE("1", "注册发送手机验证码"),
    EMAIL_VERIFICATION_CODE("2", "注册发送邮箱验证码");

    private final String code;
    private final String description;

    RegistrationStep(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RegistrationStep fromCode(String code) {
        for (RegistrationStep step : values()) {
            if (step.code.equals(code)) {
                return step;
            }
        }
        throw new IllegalArgumentException("无效的步骤代码: " + code);
    }
}
