package com.xingqiao.common.rocketmq.autoconfigure;

import com.xingqiao.common.rocketmq.config.RocketMQConfig;
import com.xingqiao.common.rocketmq.config.RocketMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * RocketMQ 自动配置类
 * 用于在其他服务引入该模块时自动加载RocketMQ相关配置
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@Import({RocketMQConfig.class})
public class RocketMQAutoConfiguration {
    
    // 自动配置类，主要通过@Import导入其他配置类
    // RocketMQConfig中包含了RocketMQ服务相关配置
    // RocketMQACLConfig中包含了RocketMQ ACL认证相关配置
    // RocketMQProperties通过@EnableConfigurationProperties启用
    
}