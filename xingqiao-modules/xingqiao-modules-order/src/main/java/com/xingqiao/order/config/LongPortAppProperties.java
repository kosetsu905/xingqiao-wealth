package com.xingqiao.order.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "longportapp.stock")
@Slf4j
public class LongPortAppProperties {

    private String appKey;
    private String appSecret;
    private String accessToken;
    private String httpUrl;
}
