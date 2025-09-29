package com.xingqiao.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * WebSocket路由配置类
 * 配置Spring Cloud Gateway转发WebSocket请求
 *
 * @author xingqiao
 * @date 2025-09-24
 */
@Configuration
public class WebSocketRouteConfig {

    /**
     * 配置WebSocket路由
     * 将ws://localhost:8080/ws/stock/**请求转发到order服务的9208端口
     */
    @Bean
    public RouteLocator webSocketRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("websocket-stock-route", r -> r
                        .path("/ws/stock/**")
                        .and().header(HttpHeaders.UPGRADE, "websocket")
                        .and().header(HttpHeaders.CONNECTION, "Upgrade")
                        // 不使用withMetadata，直接配置URI
                        .uri("lb://xingqiao-order")
                )
                .build();
    }
}