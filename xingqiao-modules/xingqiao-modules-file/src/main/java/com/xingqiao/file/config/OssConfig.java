package com.xingqiao.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig {
    private String endpoint;        // OSS 地域节点
    private String accessKeyId;     // AccessKey ID
    private String accessKeySecret; // AccessKey Secret
    private String bucketName;      // Bucket 名称
    private long timeout;           // 超时时间（毫秒）
}