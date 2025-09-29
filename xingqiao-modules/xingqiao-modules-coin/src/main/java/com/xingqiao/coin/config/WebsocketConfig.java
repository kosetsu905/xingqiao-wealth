package com.xingqiao.coin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置类
 * 
 * @author xingqiao
 */
@Configuration
public class WebsocketConfig {

    /**
     * ServerEndpointExporter 作用
     *
     * 这个Bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     * 条件注解确保该Bean只在非测试环境中创建，避免测试时出现ServerContainer not available错误
     */
    @Bean
    @ConditionalOnProperty(name = "spring.profiles.active", havingValue = "test")
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}