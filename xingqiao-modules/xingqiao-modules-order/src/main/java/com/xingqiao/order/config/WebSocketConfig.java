package com.xingqiao.order.config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 * @author zhengkai.blog.csdn.net
 */
@Configuration
public class WebSocketConfig {

    /**
     * ServerEndpointExporter 用于自动注册使用@ServerEndpoint注解的WebSocket端点
     * 条件注解确保该Bean只在非测试环境中创建，避免测试时出现ServerContainer not available错误
     * 测试的时候设置test.env属性为true
     * 不设置默认为false
     */
    @Bean
    @ConditionalOnProperty(name = "test.env", havingValue = "false", matchIfMissing = true)
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
