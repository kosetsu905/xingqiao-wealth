package com.xingqiao.message.util.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信服务配置类
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsConfig {
    /**
     * endpoint
     */
    private String endpoint = "dysmsapi.aliyuncs.com";
    
    /**
     * accessKeyId
     */
    private String accessKeyId;
    
    /**
     * accessKeySecret
     */
    private String accessKeySecret;
    
    /**
     * 短信签名
     */
    private String signName;


}