package com.xingqiao.auth.common;

import com.xingqiao.auth.form.LoginReqDTO;
import com.xingqiao.auth.service.LoginStrategy;
import com.xingqiao.system.api.enums.LoginType;
import java.util.List;
import java.util.Map;

import com.xingqiao.system.api.model.LoginUser;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class LoginContext {
    private final List<LoginStrategy> strategies;

    @Autowired
    public LoginContext(List<LoginStrategy> strategies) {
        this.strategies = strategies;
    }

    public Map<String, Object> executeLogin(LoginReqDTO request) {
        LoginStrategy strategy = getStrategy(request.getLoginType());
        return strategy.login(request);
    }

    private LoginStrategy getStrategy(LoginType type) {
        return strategies.stream()
                .filter(s -> s.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的登录方式: " + type));
    }
}
