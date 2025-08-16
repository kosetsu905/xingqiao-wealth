package com.xingqiao.auth.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope  // 支持配置动态刷新
@Component
@Data
public class AuthConfig {

    @Value("${mock.code:'xingqiao'}")
    private String mockCode;

}
