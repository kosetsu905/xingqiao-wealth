package com.xingqiao.auth.service.login;

import com.xingqiao.auth.form.LoginReqDTO;
import com.xingqiao.auth.service.LoginStrategy;
import com.xingqiao.common.core.exception.ServiceException;
import com.xingqiao.system.api.enums.LoginType;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class WechatLoginStrategy implements LoginStrategy {
    @Override
    public Map<String, Object> login(LoginReqDTO request) {
        throw new ServiceException("暂不支持该登录方式");
    }


    @Override
    public boolean supports(LoginType type) {
        return LoginType.WECHAT == type;
    }
}
