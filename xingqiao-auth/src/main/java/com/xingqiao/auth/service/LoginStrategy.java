package com.xingqiao.auth.service;

import com.xingqiao.auth.form.LoginReqDTO;
import com.xingqiao.system.api.enums.LoginType;

import java.util.Map;

public interface LoginStrategy {
    Map<String, Object> login(LoginReqDTO request);
    /**
     * 是否支持该登录类型
     * @param type 登录类型
     * @return 是否支持
     */
    boolean supports(LoginType type);

}
