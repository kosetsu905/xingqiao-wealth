package com.xingqiao.auth.common;

import com.xingqiao.auth.form.RegisterReqDTO;
import com.xingqiao.auth.service.LoginStrategy;
import com.xingqiao.auth.service.ReisterStrategy;
import com.xingqiao.system.api.enums.RegisterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RegisterContext {
    private final List<ReisterStrategy> strategies;

    @Autowired
    public RegisterContext(List<ReisterStrategy> strategies) {
        this.strategies = strategies;
    }

    public Map<String, Object> executeRegister(RegisterReqDTO request) {
        ReisterStrategy strategy = getStrategy(request.getRegisterType());
        return strategy.register(request);
    }

    private ReisterStrategy getStrategy(RegisterType type) {
        return strategies.stream()
                .filter(s -> s.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的注册方式: " + type));
    }
}
