package com.xingqiao.order.config;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 股票代码映射配置类
 * 支持从Nacos配置中心获取股票代码映射关系
 * 使用@RefreshScope确保配置更新时能自动刷新
 *
 * @author xingqiao
 */
@Setter
@Getter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "order.stock")
@Slf4j
public class StockQueryMappingConfig {

    /**
     *查询单个股票周期行情指定策略
     */
    private String stockQuoteChartStrategy;


    /**
     * 查询单个股票实时行情指定策略
     * 实时查询不缓存redis
     */
    private String stockTradeStrategy;


    /**
     *  msn查询单个股票行情开关
     */
    private boolean msnGetStockQuoteSwitch;


    /**
     *  msn查询批量股票行情开关
     */
    private boolean msnGetStockQuoteListSwitch;

}
