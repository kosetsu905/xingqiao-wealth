package com.xingqiao.auth.service;

import com.xingqiao.auth.form.RegisterReqDTO;
import com.xingqiao.system.api.enums.RegisterType;

import java.util.Map;


public interface ReisterStrategy {
    Map<String, Object> register(RegisterReqDTO request);
    /**
     * 是否支持该注册类型
     * @param type 注册类型
     * @return 是否支持
     */
    boolean supports(RegisterType type);

}
