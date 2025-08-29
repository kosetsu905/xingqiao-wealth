package com.xingqiao.message;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope  // 支持配置动态刷新
@Component
@Data
public class CommonConfig {

    @Value("${message.registerUrl}")
    private String registerUrl;
}
