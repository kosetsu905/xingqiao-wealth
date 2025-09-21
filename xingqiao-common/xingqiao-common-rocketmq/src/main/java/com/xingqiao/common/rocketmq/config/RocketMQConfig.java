package com.xingqiao.common.rocketmq.config;

import com.xingqiao.common.rocketmq.service.RocketMQMessageService;
import com.xingqiao.common.rocketmq.service.impl.RocketMQMessageServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ 配置类
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Configuration
public class RocketMQConfig {

    /**
     * 提供RocketMQMessageService的Bean定义，确保在Spring上下文可用
     * RocketMQTemplate由rocketmq-spring-boot-starter自动配置，通过application.yml文件读取配置
     */
    @Bean
    @ConditionalOnMissingBean(RocketMQMessageService.class)
    public RocketMQMessageService rocketMQMessageService() {
        return new RocketMQMessageServiceImpl();
    }
    

}