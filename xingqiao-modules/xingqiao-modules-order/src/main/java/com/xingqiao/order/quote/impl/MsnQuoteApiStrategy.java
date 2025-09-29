
package com.xingqiao.order.quote.impl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xingqiao.api.trade.domain.QueryStockQuoteList;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.config.StockCodeMappingConfig;
import com.xingqiao.order.quote.QuoteApiStrategy;
import com.xingqiao.order.utils.TimeRangeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * msn行情API策略实现类
 * 实现QuoteApiStrategy接口，提供基本的行情功能实现
 * <a href="https://www.msn.cn/">...</a>
 *
 * @author xingqiao
 * &#064;date  2025-09-23
 */
@Component
public class MsnQuoteApiStrategy implements QuoteApiStrategy {

    private static final Logger log = LoggerFactory.getLogger(MsnQuoteApiStrategy.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RedisService redisService;
    private final StockCodeMappingConfig stockCodeMappingConfig;
    private final TimeRangeUtil timeRangeUtil;

    public MsnQuoteApiStrategy(RestTemplate restTemplate, ObjectMapper objectMapper,
                               StockCodeMappingConfig stockCodeMappingConfig,
                               RedisService redisService,
                               TimeRangeUtil timeRangeUtil) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.stockCodeMappingConfig = stockCodeMappingConfig;
        this.redisService = redisService;
        this.timeRangeUtil = timeRangeUtil;
    }

    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        log.info("获取股票行情：{}", queryStockQuote);

        // 参数验证
        if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
            log.warn("股票代码不能为空");
            return R.fail("股票代码不能为空");
        }

        // 构建Redis Hash缓存键
        String hashKey = String.format("data:%s:%s:%s",
                queryStockQuote.getProductCode(),
                queryStockQuote.getMarketCode(),
                queryStockQuote.getStockCode());

        try {
            // 检查配置是否正常
            if (stockCodeMappingConfig == null) {
                log.error("StockCodeMappingConfig is NULL in getStockQuote method - Nacos configuration may not be loaded properly");
                return R.fail("系统配置错误：行情API配置未初始化，请检查Nacos配置服务连接");
            }

            // 检查API key配置
            String apiKey = stockCodeMappingConfig.getKey();
            if (apiKey == null || apiKey.isEmpty()) {
                log.error("API key is EMPTY in stockCodeMappingConfig - configuration missing");
                return R.fail("系统配置错误：行情API密钥未配置，请检查Nacos中的msn.stock.code.key设置");
            }

            // 尝试从Redis Hash中获取缓存数据
            Map<String, Object> hashMap = redisService.getCacheMap(hashKey);
            StockQuote cachedQuote = null;

            // 检查缓存是否有效
            if (!hashMap.isEmpty()) {
                // 从Hash映射转换为StockQuote对象
                cachedQuote = convertMapToStockQuote(hashMap, queryStockQuote);
                // 判断是否在开市时间
                boolean isTradingHour = timeRangeUtil.isInTradingHours(queryStockQuote.getMarketCode());
                log.debug("当前时间是否在 {} 市场开市时间内: {}", queryStockQuote.getMarketCode(), isTradingHour);
                
                // 非开市时间且缓存有数据，直接返回缓存数据
                if (!isTradingHour&&cachedQuote != null) {
                    log.info("非开市时间，直接返回缓存数据：{}", queryStockQuote.getStockCode());
                    return R.ok(cachedQuote);
                }
                
                // 开市时间，继续检查缓存是否需要更新
                if (cachedQuote != null && cachedQuote.getMsnDataTime() != null) {
                    LocalDateTime currentTime = LocalDateTime.now();
                    // 行情时间在10分钟内，继续检查缓存是否过期（2分钟）
                    if (cachedQuote.getMsnDataTime() != null) {
                        // 计算缓存时间与当前时间的差值（分钟）
                        long cacheDiffMinutes = java.time.Duration.between(cachedQuote.getMsnDataTime(), currentTime).toMinutes();
                        if (cacheDiffMinutes < stockCodeMappingConfig.getFrequency()) {
                            // 缓存未过期（2分钟内），返回缓存数据
                            log.info("从Hash缓存获取股票行情（缓存未过期）：{}", queryStockQuote.getStockCode());
                            return R.restResult(cachedQuote, 201, "获取股票行情成功");
                        } else {
                            log.info("缓存已过期（超过2分钟），重新获取股票行情：{}", queryStockQuote.getStockCode());
                        }
                    }
                }
            }

            // 获取映射后的第三方代码
            String thirdPartyCode = stockCodeMappingConfig.getThirdPartyCode(queryStockQuote);

            // 检查第三方代码是否有效
            if (thirdPartyCode == null) {
                log.warn("Failed to get third party code for: {}", queryStockQuote);
                return R.fail("获取行情失败：股票代码无效");
            }

            // 构建请求URL
            String url = String.format(stockCodeMappingConfig.getUrl(), stockCodeMappingConfig.getKey(), stockCodeMappingConfig.getId(), thirdPartyCode);

            // 创建HTTP头
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");

            // 发送请求
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 解析响应
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // 检查响应是否包含数据
            if (rootNode.isEmpty() || !rootNode.isArray()) {
                log.error("MSN API返回空数据：{}", response.getBody());
                return R.fail("获取股票行情失败：无数据返回");
            }

            // 获取第一条数据
            JsonNode quoteData = rootNode.get(0);
            if (quoteData == null) {
                log.warn("MSN API返回格式不正确：{}", response.getBody());
                return R.fail("获取股票行情失败：数据格式不正确");
            }

            // 转换为StockQuote对象
            StockQuote stockQuote = convertToStockQuote(quoteData, queryStockQuote);

            // 更新数据时间为当前时间（用于缓存过期检查）
            stockQuote.setMsnDataTime(LocalDateTime.now());

            // 将StockQuote转换为Map并保存到Redis Hash
            Map<String, Object> stockQuoteMap = convertStockQuoteToMap(stockQuote);
            redisService.setCacheMap(hashKey, stockQuoteMap);


            log.info("获取股票行情成功并保存到Hash缓存：{}", queryStockQuote.getStockCode());
            return R.ok(stockQuote);

        } catch (Exception e) {
            log.error("获取股票行情异常：", e);
            return R.fail("获取股票行情失败：" + e.getMessage());
        }
    }

    /**
     * 将StockQuote对象转换为Map，用于存储到Redis Hash
     */
    private Map<String, Object> convertStockQuoteToMap(StockQuote stockQuote) {
        Map<String, Object> map = new HashMap<>();

        // 基础信息
        map.put("productCode", stockQuote.getProductCode());
        map.put("marketCode", stockQuote.getMarketCode());
        map.put("stockCode", stockQuote.getStockCode());
        map.put("stockName", stockQuote.getStockName());
        

        // 价格信息
        if (stockQuote.getCurrentPrice() != null) {
            map.put("currentPrice", stockQuote.getCurrentPrice().toString());
        }
        if (stockQuote.getPriceChange() != null) {
            map.put("priceChange", stockQuote.getPriceChange().toString());
        }
        if (stockQuote.getPriceChangePercent() != null) {
            map.put("priceChangePercent", stockQuote.getPriceChangePercent().toString());
        }
        if (stockQuote.getHighPrice() != null) {
            map.put("highPrice", stockQuote.getHighPrice().toString());
        }
        if (stockQuote.getLowPrice() != null) {
            map.put("lowPrice", stockQuote.getLowPrice().toString());
        }
        if (stockQuote.getOpenPrice() != null) {
            map.put("openPrice", stockQuote.getOpenPrice().toString());
        }
        if (stockQuote.getPrevClosePrice() != null) {
            map.put("prevClosePrice", stockQuote.getPrevClosePrice().toString());
        }
        // 新增收盘价
        if (stockQuote.getClosePrice() != null) {
            map.put("closePrice", stockQuote.getClosePrice().toString());
        }

        // 52周高低价
        if (stockQuote.getWeek52High() != null) {
            map.put("week52High", stockQuote.getWeek52High().toString());
        }
        if (stockQuote.getWeek52Low() != null) {
            map.put("week52Low", stockQuote.getWeek52Low().toString());
        }

        // 买卖盘信息
        if (stockQuote.getBidPrice1() != null) {
            map.put("bidPrice1", stockQuote.getBidPrice1().toString());
        }
        if (stockQuote.getBidVolume1() != null) {
            map.put("bidVolume1", stockQuote.getBidVolume1().toString());
        }
        if (stockQuote.getAskPrice1() != null) {
            map.put("askPrice1", stockQuote.getAskPrice1().toString());
        }
        if (stockQuote.getAskVolume1() != null) {
            map.put("askVolume1", stockQuote.getAskVolume1().toString());
        }


        // 成交量
        if (stockQuote.getVolume() != null) {
            map.put("volume", stockQuote.getVolume().toString());
        }

        // 市场和交易所信息
        map.put("marketName", stockQuote.getMarketName());
        map.put("productName", stockQuote.getProductName());
        map.put("status", stockQuote.getStatus());
        map.put("dataSource", stockQuote.getDataSource());

        // 时间信息
        if (stockQuote.getMsnDataTime() != null) {
            map.put("msnDataTime", stockQuote.getMsnDataTime().toString());
        }
        if (stockQuote.getTimeLastUpdated() != null) {
            map.put("timeLastUpdated", stockQuote.getTimeLastUpdated().toString());
        }

        return map;
    }

    /**
     * 将Redis Hash中的Map转换为StockQuote对象
     */
    private StockQuote convertMapToStockQuote(Map<String, Object> map, QueryStockQuote queryStockQuote) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        StockQuote stockQuote = new StockQuote();

        // 如果Map中没有基础信息，使用查询参数填充
        if (queryStockQuote != null) {
            if (map.get("productCode") == null) {
                stockQuote.setProductCode(queryStockQuote.getProductCode());
            }
            if (map.get("marketCode") == null) {
                stockQuote.setMarketCode(queryStockQuote.getMarketCode());
            }
            if (map.get("stockCode") == null) {
                stockQuote.setStockCode(queryStockQuote.getStockCode());
            }
        }
        // 基础信息
        stockQuote.setProductCode((String) map.getOrDefault("productCode", stockQuote.getProductCode()));
        stockQuote.setMarketCode((String) map.getOrDefault("marketCode", stockQuote.getMarketCode()));
        stockQuote.setStockCode((String) map.getOrDefault("stockCode", stockQuote.getStockCode()));
        stockQuote.setStockName((String) map.get("stockName"));

        // 价格信息 - 从字符串转换为BigDecimal
        try {
            if (map.get("currentPrice") != null) {
                stockQuote.setCurrentPrice(new BigDecimal(map.get("currentPrice").toString()));
            }
            if (map.get("priceChange") != null) {
                stockQuote.setPriceChange(new BigDecimal(map.get("priceChange").toString()));
            }
            if (map.get("priceChangePercent") != null) {
                stockQuote.setPriceChangePercent(new BigDecimal(map.get("priceChangePercent").toString()));
            }
            if (map.get("highPrice") != null) {
                stockQuote.setHighPrice(new BigDecimal(map.get("highPrice").toString()));
            }
            if (map.get("lowPrice") != null) {
                stockQuote.setLowPrice(new BigDecimal(map.get("lowPrice").toString()));
            }
            if (map.get("openPrice") != null) {
                stockQuote.setOpenPrice(new BigDecimal(map.get("openPrice").toString()));
            }
            if (map.get("prevClosePrice") != null) {
                stockQuote.setPrevClosePrice(new BigDecimal(map.get("prevClosePrice").toString()));
            }
            // 新增收盘价
            if (map.get("closePrice") != null) {
                stockQuote.setClosePrice(new BigDecimal(map.get("closePrice").toString()));
            }

            // 52周高低价
            if (map.get("week52High") != null) {
                stockQuote.setWeek52High(new BigDecimal(map.get("week52High").toString()));
            }
            if (map.get("week52Low") != null) {
                stockQuote.setWeek52Low(new BigDecimal(map.get("week52Low").toString()));
            }

            // 买卖盘信息
            if (map.get("bidPrice1") != null) {
                stockQuote.setBidPrice1(new BigDecimal(map.get("bidPrice1").toString()));
            }
            if (map.get("bidVolume1") != null) {
                stockQuote.setBidVolume1(Long.valueOf(map.get("bidVolume1").toString()));
            }
            if (map.get("askPrice1") != null) {
                stockQuote.setAskPrice1(new BigDecimal(map.get("askPrice1").toString()));
            }
            if (map.get("askVolume1") != null) {
                stockQuote.setAskVolume1(Long.valueOf(map.get("askVolume1").toString()));
            }

            // 成交量
            if (map.get("volume") != null) {
                stockQuote.setVolume(Long.valueOf(map.get("volume").toString()));
            }

            // 市场和交易所信息
            stockQuote.setMarketName((String) map.get("marketName"));
            stockQuote.setProductName((String) map.get("productName"));
            if (map.get("status") != null) {
                try {
                    stockQuote.setStatus(Integer.valueOf(map.get("status").toString()));
                } catch (NumberFormatException e) {
                    log.warn("状态字段格式错误: {}", map.get("status"));
                }
            }
            stockQuote.setDataSource((String) map.get("dataSource"));

            // 时间信息 - 从字符串转换为LocalDateTime
            if (map.get("msnDataTime") != null) {
                try {
                    stockQuote.setMsnDataTime(LocalDateTime.parse(map.get("msnDataTime").toString()));
                } catch (Exception e) {
                    log.warn("dataTime格式错误: {}", map.get("msnDataTime"));
                }
            }
            if (map.get("timeLastUpdated") != null) {
                try {
                    stockQuote.setTimeLastUpdated(LocalDateTime.parse(map.get("timeLastUpdated").toString()));
                } catch (Exception e) {
                    log.warn("timeLastUpdated格式错误: {}", map.get("timeLastUpdated"));
                }
            }
        } catch (Exception e) {
            log.error("从Redis Hash转换StockQuote对象异常：", e);
            return null;
        }

        return stockQuote;
    }


    /**
     * 将MSN API响应转换为StockQuote对象
     *
     * @param quoteNode API响应数据
     * @return StockQuote对象
     */
    private StockQuote convertToStockQuote(JsonNode quoteNode, QueryStockQuote queryStockQuote) {
        StockQuote stockQuote = new StockQuote();
        stockQuote.setStockCode(queryStockQuote.getStockCode());
        stockQuote.setMarketCode(queryStockQuote.getMarketCode());

        if (quoteNode != null) {
            // 价格相关信息 - 使用getOrDefault避免null值直接设置
            stockQuote.setCurrentPrice(getBigDecimalFromNode(quoteNode, "price"));
            stockQuote.setPriceChange(getBigDecimalFromNode(quoteNode, "priceChange"));
            stockQuote.setPriceChangePercent(getBigDecimalFromNode(quoteNode, "priceChangePercent"));
            stockQuote.setHighPrice(getBigDecimalFromNode(quoteNode, "priceDayHigh"));
            stockQuote.setLowPrice(getBigDecimalFromNode(quoteNode, "priceDayLow"));
            stockQuote.setOpenPrice(getBigDecimalFromNode(quoteNode, "priceDayOpen"));
            stockQuote.setPrevClosePrice(getBigDecimalFromNode(quoteNode, "pricePreviousClose"));

            // 添加收盘价字段
            stockQuote.setClosePrice(getBigDecimalFromNode(quoteNode, "priceClose"));

            // 52周高低价
            stockQuote.setWeek52High(getBigDecimalFromNode(quoteNode, "price52wHigh"));
            stockQuote.setWeek52Low(getBigDecimalFromNode(quoteNode, "price52wLow"));

            // 确保买卖盘价格和数量也使用getOrDefault
            stockQuote.setBidPrice1(getBigDecimalFromNode(quoteNode, "bidPrice"));
            stockQuote.setBidVolume1(getLongFromNode(quoteNode, "bidVolume"));
            stockQuote.setAskPrice1(getBigDecimalFromNode(quoteNode, "askPrice"));
            stockQuote.setAskVolume1(getLongFromNode(quoteNode, "askVolume"));

            // 股票名称
            String displayName = getStringFromNode(quoteNode, "displayName");
            String shortName = getStringFromNode(quoteNode, "shortName");

            // 尝试获取中文名称
            if (quoteNode.has("localizedAttributes") && quoteNode.get("localizedAttributes").has("zh-cn")) {
                JsonNode zhCnNode = quoteNode.get("localizedAttributes").get("zh-cn");
                displayName = getStringFromNode(zhCnNode, "displayName");
                shortName = getStringFromNode(zhCnNode, "shortName");
            }

            // 只在有值时设置股票名称
            if (displayName != null) {
                stockQuote.setStockName(displayName);
            } else if (shortName != null) {
                stockQuote.setStockName(shortName);
            } else {
                stockQuote.setStockName(queryStockQuote.getStockCode());
            }

            // 市场名称 - 只在有值时设置
            String marketName = getStringFromNode(quoteNode, "exchangeName");
            if (marketName != null) {
                stockQuote.setMarketName(marketName);
            }

            // 产品代码
            String securityType = getStringFromNode(quoteNode, "securityType");
            if (securityType != null) {
                stockQuote.setProductCode("index".equalsIgnoreCase(securityType) ? "indices" : "stock");
            } else {
                stockQuote.setProductCode("stock"); // 默认值
            }

            // 产品名称 - 只在有值时设置
            String productName = getStringFromNode(quoteNode, "sourceExchangeName");
            if (productName != null) {
                stockQuote.setProductName(productName);
            }

            // 多周期价格变化和回报率 - 存储为扩展字段
            Map<String, Object> extendInfo = new HashMap<>();
            BigDecimal priceChange1Week = getBigDecimalFromNode(quoteNode, "priceChange1Week");
            if (priceChange1Week != null) {
                extendInfo.put("priceChange1Week", priceChange1Week);
            }
            BigDecimal priceChange1Month = getBigDecimalFromNode(quoteNode, "priceChange1Month");
            if (priceChange1Month != null) {
                extendInfo.put("priceChange1Month", priceChange1Month);
            }
            BigDecimal priceChange3Month = getBigDecimalFromNode(quoteNode, "priceChange3Month");
            if (priceChange3Month != null) {
                extendInfo.put("priceChange3Month", priceChange3Month);
            }
            BigDecimal priceChange6Month = getBigDecimalFromNode(quoteNode, "priceChange6Month");
            if (priceChange6Month != null) {
                extendInfo.put("priceChange6Month", priceChange6Month);
            }
            BigDecimal priceChangeYTD = getBigDecimalFromNode(quoteNode, "priceChangeYTD");
            if (priceChangeYTD != null) {
                extendInfo.put("priceChangeYTD", priceChangeYTD);
            }
            BigDecimal priceChange1Year = getBigDecimalFromNode(quoteNode, "priceChange1Year");
            if (priceChange1Year != null) {
                extendInfo.put("priceChange1Year", priceChange1Year);
            }
            BigDecimal return1Week = getBigDecimalFromNode(quoteNode, "return1Week");
            if (return1Week != null) {
                extendInfo.put("return1Week", return1Week);
            }
            BigDecimal return1Month = getBigDecimalFromNode(quoteNode, "return1Month");
            if (return1Month != null) {
                extendInfo.put("return1Month", return1Month);
            }
            BigDecimal return3Month = getBigDecimalFromNode(quoteNode, "return3Month");
            if (return3Month != null) {
                extendInfo.put("return3Month", return3Month);
            }
            BigDecimal return6Month = getBigDecimalFromNode(quoteNode, "return6Month");
            if (return6Month != null) {
                extendInfo.put("return6Month", return6Month);
            }
            BigDecimal returnYTD = getBigDecimalFromNode(quoteNode, "returnYTD");
            if (returnYTD != null) {
                extendInfo.put("returnYTD", returnYTD);
            }
            BigDecimal return1Year = getBigDecimalFromNode(quoteNode, "return1Year");
            if (return1Year != null) {
                extendInfo.put("return1Year", return1Year);
            }


            // 最后交易时间
            String lastTraded = getStringFromNode(quoteNode, "timeLastTraded");
            if (lastTraded != null) {
                try {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(lastTraded);
                    stockQuote.setTimeLastUpdated(zonedDateTime.toLocalDateTime());
                } catch (Exception e) {
                    log.warn("解析最后交易时间格式失败：{}", lastTraded);
                }
            }
            // 获取时间
            stockQuote.setMsnDataTime(LocalDateTime.now());

            // 状态默认设为正常
            stockQuote.setStatus(0);

            // 数据源
            stockQuote.setDataSource("MSN");
        }

        return stockQuote;
    }

    /**
     * 从JsonNode中获取BigDecimal值
     */
    private BigDecimal getBigDecimalFromNode(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName) && !node.get(fieldName).isNull() && node.get(fieldName).isNumber()) {
            return BigDecimal.valueOf(node.get(fieldName).asDouble());
        }
        return null;
    }

    /**
     * 从JsonNode中获取Long值
     */
    private Long getLongFromNode(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName) && !node.get(fieldName).isNull() && node.get(fieldName).isIntegralNumber()) {
            return node.get(fieldName).asLong();
        }
        return null;
    }

    /**
     * 从JsonNode中获取String值
     */
    private String getStringFromNode(JsonNode node, String fieldName) {
        if (node != null && node.has(fieldName) && !node.get(fieldName).isNull() && node.get(fieldName).isTextual()) {
            return node.get(fieldName).asText();
        }
        return null;
    }

    @Override
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list) {
        log.info("批量获取股票行情，数量：{}", list != null ? list.size() : 0);
    
        // 参数验证
        if (list == null || list.isEmpty()) {
            log.warn("股票代码列表不能为空");
            return R.fail("股票代码列表不能为空");
        }
    
        try {
            // 分离需要查询的股票和已有缓存的股票
            List<QueryStockQuote> needQueryList = new ArrayList<>();
            List<StockQuote> resultList = new ArrayList<>();
            // 创建返回实体
            LocalDateTime currentTime = LocalDateTime.now();
    
            for (QueryStockQuote queryStockQuote : list) {
                if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
                    continue;
                }
                // 构建缓存键
                String hashKey = String.format("data:%s:%s:%s",
                        queryStockQuote.getProductCode(),
                        queryStockQuote.getMarketCode(),
                        queryStockQuote.getStockCode());
    
                // 尝试从缓存获取
                Map<String, Object> hashMap = redisService.getCacheMap(hashKey);
                if (!hashMap.isEmpty()) {
                    // 缓存存在，转换为StockQuote对象
                    StockQuote cachedQuote = convertMapToStockQuote(hashMap, queryStockQuote);
    
                    // 判断是否在开市时间
                    boolean isTradingHour = timeRangeUtil.isInTradingHours(queryStockQuote.getMarketCode());
                    log.debug("当前时间是否在 {} 市场开市时间内: {}", queryStockQuote.getMarketCode(), isTradingHour);
                    
                    // 非开市时间且缓存有数据，直接返回缓存数据
                    if (!isTradingHour&&cachedQuote!=null) {
                        log.info("批量查询 - 非开市时间，直接返回缓存数据：{}", queryStockQuote.getStockCode());
                        resultList.add(cachedQuote);
                        continue;
                    }
                    //判断msndateTime是否在2分钟内，如果是返回缓存数据,不是重新去请求数据
                    if (cachedQuote != null && cachedQuote.getMsnDataTime() != null) {
                        long diffMinutes = java.time.Duration.between(cachedQuote.getMsnDataTime(), currentTime).toMinutes();
                        if (diffMinutes <= stockCodeMappingConfig.getFrequency()) {
                            log.info("返回缓存股票行情数据（根据msnDataTime）：{}", queryStockQuote.getStockCode());
                            resultList.add(cachedQuote);
                            continue;
                        }
                    }
                }
                // 需要重新查询
                needQueryList.add(queryStockQuote);
            }
    
            // 如果有需要查询的股票
            if (!needQueryList.isEmpty()) {
                // 构建批量查询参数
                StringBuilder thirdPartyCodesBuilder = new StringBuilder();
                Map<String, QueryStockQuote> thirdPartyCodeMap = new HashMap<>();
    
                for (QueryStockQuote queryStockQuote : needQueryList) {
                    String thirdPartyCode = stockCodeMappingConfig.getThirdPartyCode(queryStockQuote);
                    if (thirdPartyCode != null) {
                        if (thirdPartyCodesBuilder.length() > 0) {
                            thirdPartyCodesBuilder.append(",");
                        }
                        thirdPartyCodesBuilder.append(thirdPartyCode);
                        // 保存第三方代码与查询对象的映射
                        thirdPartyCodeMap.put(thirdPartyCode, queryStockQuote);
                    } else {
                        log.warn("Invalid third party code for stock: {}", queryStockQuote.getStockCode());
                    }
                }
    
                // 检查是否有有效的第三方代码可查询
                if (thirdPartyCodesBuilder.length() == 0) {
                    log.warn("No valid third party codes for batch query");
                    // 设置结果列表到返回实体
                    return R.ok(resultList);
                }
    
                // 构建批量请求URL
                String url = String.format(stockCodeMappingConfig.getUrl(), stockCodeMappingConfig.getKey(), stockCodeMappingConfig.getId(), thirdPartyCodesBuilder.toString());
    
                // 创建HTTP头
                HttpHeaders headers = new HttpHeaders();
                headers.set("Accept", "application/json");
    
                // 发送请求
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    
                // 解析响应
                JsonNode rootNode = objectMapper.readTree(response.getBody());
    
                // 设置当前时间为msnDataTime和itickDataTime
                currentTime = LocalDateTime.now();
    
                // 处理批量响应
                if (rootNode != null && rootNode.isArray()) {
                    for (int i = 0; i < rootNode.size(); i++) {
                        JsonNode itemArray = rootNode.get(i);
                        if (itemArray != null && itemArray.isArray() && !itemArray.isEmpty()) {
                            JsonNode quoteData = itemArray.get(0);
                            if (quoteData != null && quoteData.has("instrumentId")) {
                                String instrumentId = quoteData.get("instrumentId").asText();
                                QueryStockQuote queryStockQuote = thirdPartyCodeMap.get(instrumentId);
    
                                if (queryStockQuote != null) {
                                    // 转换为StockQuote对象
                                    StockQuote stockQuote = convertToStockQuote(quoteData, queryStockQuote);
                                    // 设置msnDataTime
                                    stockQuote.setMsnDataTime(currentTime);
                                    // 保存到缓存
                                    String hashKey = String.format("data:%s:%s:%s",
                                            queryStockQuote.getProductCode(),
                                            queryStockQuote.getMarketCode(),
                                            queryStockQuote.getStockCode());
                                    Map<String, Object> stockQuoteMap = convertStockQuoteToMap(stockQuote);
                                    redisService.setCacheMap(hashKey, stockQuoteMap);
                                    // 添加到结果列表
                                    resultList.add(stockQuote);
                                }
                            }
                        }
                    }
                }
            }
    
            // 设置结果列表到返回实体
            log.info("批量获取股票行情成功，返回数量：{}", resultList.size());
            return R.ok(resultList);
    
        } catch (Exception e) {
            log.error("批量获取股票行情异常：", e);
            return R.fail("批量获取股票行情失败：" + e.getMessage());
        }
    }

    @Override
    public R<List<StockQuote>> getStockQuoteHistory(QueryStockQuote queryStockQuote) {
        return null;
    }

    @Override
    public R subscribeStockQuote(QueryStockQuoteList queryStockQuoteList) {
        return null;
    }

    @Override
    public R unsubscribeStockQuote(QueryStockQuoteList queryStockQuoteList) {
        return null;
    }

    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    @Override
    public String getStrategyName() {
        return "msn";
    }
}
