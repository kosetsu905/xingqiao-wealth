package com.xingqiao.order.config;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * 股票代码映射配置类
 * 支持从Nacos配置中心获取股票代码映射关系
 * 使用@RefreshScope确保配置更新时能自动刷新
 * 
 * @author xingqiao
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "msn.stock.code")
@NacosConfigurationProperties(dataId = "application-msn-${spring.profiles.active}.yml", autoRefreshed = true)
@Slf4j
public class StockCodeMappingConfig  {

    /**
     * 获取股票行情频率
     * 秒
     */
    @Getter
    @Setter
    private Integer frequency;


    @Getter
    @Setter
    private String listUrl;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String key;

    @Getter
    @Setter
    private String id;


    @Getter
    @Setter
    private boolean switchOn;

    /**
     * 股票代码映射关系
     * 格式: marketCode+stockCode -> thirdPartyCode
     */
    private Map<String, String> mappings = new HashMap<>();
    
    /**
     * 设置股票代码映射关系
     * 
     * @param mappings 股票代码映射关系Map
     */
    public void setMappings(Map<String, String> mappings) {
        this.mappings = mappings != null ? mappings : new HashMap<>();
        log.info("Stock code mappings loaded: size={}", this.mappings.size());
    }
    
    /**
     * 获取股票代码映射关系
     * 
     * @return 股票代码映射关系Map
     */
    public Map<String, String> getMappings() {
        return new HashMap<>(mappings);
    }
    
    /**
     * 根据市场代码和股票代码获取第三方股票代码
     * 如果没有找到映射关系，则返回原始代码
     *
     * @return 第三方股票代码
     */
    public String getThirdPartyCode(QueryStockQuote queryStockQuote) {
        if (queryStockQuote == null || StringUtils.isEmpty(queryStockQuote.getProductCode())
                || StringUtils.isEmpty(queryStockQuote.getMarketCode()) || StringUtils.isEmpty(queryStockQuote.getStockCode())) {
            log.warn("Market code or stock code is null or empty");
            return null;
        }
        
        try {
            String key = queryStockQuote.getProductCode() + "_" +
                    queryStockQuote.getMarketCode() + "_" + queryStockQuote.getStockCode();
            String mappedCode = mappings.getOrDefault(key, queryStockQuote.getStockCode());
            
            if (!queryStockQuote.getStockCode().equals(mappedCode)) {
                log.debug("Stock code mapped: original={}, mapped={}", queryStockQuote.getStockCode(), mappedCode);
            }
            
            return StringUtils.isEmpty(mappedCode) ? null : mappedCode;
        } catch (Exception e) {
            log.error("Error while getting third party code", e);
            return null;
        }
    }
    
    /**
     * 反向查找原始股票代码
     * 用于从第三方代码反向映射到原始代码
     * 
     * @param marketCode 市场代码
     * @param thirdPartyCode 第三方股票代码
     * @return 原始股票代码，如果未找到则返回第三方代码
     */
    public String getOriginalCode(String marketCode, String thirdPartyCode) {
        if (marketCode == null || thirdPartyCode == null) {
            log.warn("Market code or third party code is null");
            return thirdPartyCode;
        }
        
        try {
            // 遍历映射表，查找反向映射
            for (Map.Entry<String, String> entry : mappings.entrySet()) {
                // 检查键是否以市场代码开头，并且值等于第三方代码
                if (entry.getKey().startsWith(marketCode) && entry.getValue().equals(thirdPartyCode)) {
                    // 提取原始股票代码（去掉市场代码前缀）
                    String originalCode = entry.getKey().substring(marketCode.length());
                    log.debug("Reverse stock code mapped: thirdParty={}, original={}{}", 
                              thirdPartyCode, marketCode, originalCode);
                    return originalCode;
                }
            }
            
            // 如果未找到映射，返回第三方代码
            return thirdPartyCode;
        } catch (Exception e) {
            log.error("Error while getting original code", e);
            return thirdPartyCode;
        }
    }

}