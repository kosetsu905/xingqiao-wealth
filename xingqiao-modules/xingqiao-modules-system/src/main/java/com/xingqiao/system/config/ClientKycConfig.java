package com.xingqiao.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * KYC配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "client.kyc")
public class ClientKycConfig {

    /**
     * 场景ID
     */
    private Long sceneId;

    /**
     * 产品码
     */
    private String productCode;

    /**
     * 模式
     */
    private String model;

    /**
     * 证件类型
     */
    private String certType;

    /**
     * 回调地址
     */
    private String returnUrl;

    /**
     * OSS存储桶名称
     */
    private String ossBucketName;

    public String accessKeyId;

    public String accessKeySecret;
    public String endpoint;
}