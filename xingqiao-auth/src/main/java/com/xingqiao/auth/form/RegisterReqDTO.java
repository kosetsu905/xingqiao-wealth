package com.xingqiao.auth.form;

import com.xingqiao.system.api.enums.RegisterType;
import lombok.Data;

@Data
public class RegisterReqDTO {
    /**
     * 登录账号
     */
    private String account;

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
     * 用户密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String comfirmPassword;

    /**
     * 验证码
     */
    private String code;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 注册类型
     */
    private RegisterType registerType;


    private String userName;


    private String nickName;


}
