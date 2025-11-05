package com.xingqiao.order.config;


import com.longport.Config;
import com.longport.ConfigBuilder;
import com.longport.quote.QuoteContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;

/**
 * 行情服务Spring Boot自动配置类
 * 负责自动配置和管理QuoteContext的生命周期
 * 
 * @author Java后端架构师
 */
@Slf4j
@Configuration
public class QuoteAutoConfiguration {

    private QuoteContext quoteContext;

    /**
     * 创建行情服务配置对象
     * 
     * @param properties 配置属性
     * @return Config 行情服务配置对象
     */
    @Bean
    public Config quoteConfig(LongPortAppProperties properties) {
        // 验证配置参数是否完整
        Assert.hasText(properties.getAppKey(), "API key must not be empty");
        Assert.hasText(properties.getAppSecret(), "API secret must not be empty");
        Assert.hasText(properties.getAccessToken(), "Access token must not be empty");
        try{
            // 构建配置对象
            Config config = new ConfigBuilder(
                    properties.getAppKey(),
                    properties.getAppSecret(),
                    properties.getAccessToken()
            ).build();
            log.info("Quote service config initialized successfully");
            return config;
        }catch (Exception e){
            log.error("行情服务配置异常"+e);
            return null;
        }
    }

    /**
     * 创建行情上下文对象，负责行情连接管理
     * 
     * @param config 配置对象
     * @return QuoteContext 行情上下文对象
     */
    @Bean
    public QuoteContext quoteContext(Config config, LongPortAppProperties properties) {
        try {
            // 使用CompletableFuture获取异步创建的QuoteContext
            CompletableFuture<QuoteContext> future = QuoteContext.create(config);
            quoteContext = future.get();
            log.info("QuoteContext initialized successfully");
            return quoteContext;
        } catch (Exception e) {
            log.error("Failed to initialize QuoteContext", e);
            throw new RuntimeException("Failed to initialize QuoteContext", e);
        }
    }

    /**
     * 在应用关闭时释放行情连接资源
     * 使用@PreDestroy确保容器关闭时正确释放资源
     */
    @PreDestroy
    public void destroy() {
        if (quoteContext != null) {
            try {
                quoteContext.close();
                log.info("QuoteContext closed successfully");
            } catch (Exception e) {
                log.error("Error closing QuoteContext", e);
            }
        }
    }
}