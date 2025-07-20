package com.xingqiao.auth.form;

import lombok.Data;

@Data
public class CodeReqDTO {
    /**
     * 手机国家区号
     */
    private String countryCode;

    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 邮箱
     */
    private String email;


    /**
     * 步骤，1：注册发送手机验证码，2：注册发送邮箱验证码
     */
    private String step;


}
