package com.xingqiao.auth.form;

import com.xingqiao.auth.annotation.PhoneOrEmailRequired;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@PhoneOrEmailRequired
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
    @NotEmpty(message = "执行步骤不能为空")
    private String step;


}
