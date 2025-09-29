package com.xingqiao.order.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 股票开市时间配置类
 * 用于从Nacos读取各市场的开市时间配置
 *
 * @author xingqiao
 * @date 2025-09-23
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "stock")
public class StockTradingHoursConfig {
    
    /**
     * 各市场开市时间映射
     * 格式如: {"SH":"09:15:00-11:30:00,13:00:00-15:00:00", "SZ":"...", "US":"..."}
     * -- GETTER --
     *  获取所有市场的开市时间配置
     *
     *
     * -- SETTER --
     *  设置所有市场的开市时间配置
     *
     @return 开市时间映射
      * @param opens 开市时间映射

     */
    private Map<String, String> opens;

    /**
     * 获取市场的开市时间配置
     * @param marketCode 市场代码
     * @return 开市时间配置字符串
     */
    public String getOpenTimeByMarketCode(String marketCode) {
        return opens != null ? opens.get(marketCode) : null;
    }

}