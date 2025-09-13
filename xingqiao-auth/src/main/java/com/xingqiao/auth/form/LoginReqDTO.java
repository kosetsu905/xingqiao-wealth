package com.xingqiao.auth.form;

import com.xingqiao.system.api.enums.LoginType;
import lombok.Data;


@Data
public class LoginReqDTO {
    /**
     * 手机国家区号
     */
    private String countryCode;

    /**
     * 手机号
     */
    private String userName;


    /**
     * 用户密码
     */
    private String password;

    /**
     * 第三方登录授权码
     */
    private String authCode;

    /**
     * 验证码
     */
    private String code;

    /**
     * 登录方式
     */
    private LoginType loginType;

    /**
     * 用户类型 01-客户 02-业务员
     */
    private String userType;


}
