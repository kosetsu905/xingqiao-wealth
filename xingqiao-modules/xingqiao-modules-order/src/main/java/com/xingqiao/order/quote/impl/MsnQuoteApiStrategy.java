
package com.xingqiao.order.quote.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xingqiao.api.trade.domain.QueryStockQuoteList;
import com.xingqiao.api.trade.domain.StockChartQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.config.StockCodeMappingConfig;
import com.xingqiao.order.config.StockQueryMappingConfig;
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
import java.util.stream.Collectors;

import static com.xingqiao.order.config.TradeConstants.*;

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
    private final StockQueryMappingConfig stockQueryMappingConfig;

    public MsnQuoteApiStrategy(RestTemplate restTemplate, ObjectMapper objectMapper,
                               StockCodeMappingConfig stockCodeMappingConfig,
                               RedisService redisService,
                               StockQueryMappingConfig stockQueryMappingConfig,
                               TimeRangeUtil timeRangeUtil) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.stockCodeMappingConfig = stockCodeMappingConfig;
        this.stockQueryMappingConfig = stockQueryMappingConfig;
        this.redisService = redisService;
        this.timeRangeUtil = timeRangeUtil;
    }

    @Override
    public R<StockQuote> getStockCurrentQuote(QueryStockQuote queryStockQuote) {
        return null;
    }

    /**
     * 获取单个股票行情
     */
    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {


        if (!stockQueryMappingConfig.isMsnGetStockQuoteSwitch()){
            return R.fail("获取股票行情失败：行情API未启用");
        }

        log.info("获取股票行情：{}", queryStockQuote);
        // 参数验证
        if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
            log.warn("股票代码不能为空");
            return R.fail("股票代码不能为空");
        }

        // 构建Redis Hash缓存键
        String redisKey = getRedisKey(queryStockQuote);

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
            StockQuote cachedQuote = redisService.getCacheObject(redisKey);

            // 检查缓存是否有效
            if (!Objects.isNull(cachedQuote)) {
                // 判断是否在开市时间
                boolean isTradingHour = timeRangeUtil.isInTradingHours(queryStockQuote.getMarketCode());
                log.debug("当前时间是否在 {} 市场开市时间内: {}", queryStockQuote.getMarketCode(), isTradingHour);

                // 非开市时间且缓存有数据，直接返回缓存数据
                if (!isTradingHour) {
                    log.info("非开市时间，直接返回缓存数据：{}", queryStockQuote.getStockCode());
                    return R.ok(cachedQuote);
                }

                // 开市时间，继续检查缓存是否需要更新，2分钟内返回缓存数据，2分钟可以nacos动态配置
                if (cachedQuote.getMsnDataTime() != null) {
                    LocalDateTime currentTime = LocalDateTime.now();
                    // 行情时间内，继续检查缓存是否过期（2分钟）
                    if (cachedQuote.getMsnDataTime() != null) {
                        // 计算缓存时间与当前时间的差值（2分钟）
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

            //保存获取的股票数据到Redis
            redisService.setCacheObject(redisKey, stockQuote);

            log.info("获取股票行情成功并保存到Hash缓存：{}", queryStockQuote.getStockCode());
            return R.ok(stockQuote);

        } catch (Exception e) {
            log.error("获取股票行情异常：", e);
            return R.fail("获取股票行情失败：" + e.getMessage());
        }
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
                String redisKey = getRedisKey(queryStockQuote);

                // 尝试从缓存获取
                StockQuote cachedQuote = redisService.getCacheObject(redisKey);
                if (!Objects.isNull(cachedQuote)) {
                    // 判断是否在开市时间
                    boolean isTradingHour = timeRangeUtil.isInTradingHours(queryStockQuote.getMarketCode());
                    log.debug("当前时间是否在 {} 市场开市时间内: {}", queryStockQuote.getMarketCode(), isTradingHour);
                    // 非开市时间且缓存有数据，直接返回缓存数据
                    if (!isTradingHour) {
                        log.info("批量查询 - 非开市时间，直接返回缓存数据：{}", queryStockQuote.getStockCode());
                        resultList.add(cachedQuote);
                        continue;
                    }
                    //判断msndateTime是否在2分钟内，如果是返回缓存数据,不是重新去请求数据
                    if (cachedQuote.getMsnDataTime() != null) {
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
                String url = String.format(stockCodeMappingConfig.getUrl(), stockCodeMappingConfig.getKey(), stockCodeMappingConfig.getId(), thirdPartyCodesBuilder, "Quotes");

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
                                    String hashKey = getRedisKey(queryStockQuote);
                                    redisService.setCacheObject(hashKey, stockQuote);
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


    /**
     * 数据量大不保存redis
     * @param list
     * @return
     */
    @Override
    public R<List<StockQuote>> getStockQuoteChartList(List<QueryStockQuote> list) {


        if (!stockQueryMappingConfig.isMsnGetStockQuoteListSwitch()){
            return R.fail("获取股票行情失败：行情API未启用");
        }

        log.info("批量获取股票图表行情");
        // 参数验证
        if (list == null || list.isEmpty()) {
            log.warn("股票代码列表不能为空");
            return R.fail("股票代码列表不能为空");
        }
        try {
            List<StockQuote> resultList = new ArrayList<>();

            //当前行情数据
            getStockQuoteList(list);

            // 根据type对needQueryList进行分组
            Map<String, List<QueryStockQuote>> groupedByType = list.stream()
                    .collect(Collectors.groupingBy(QueryStockQuote::getType, Collectors.toList()));

            // 遍历每个分组进行处理
            for (Map.Entry<String, List<QueryStockQuote>> entry : groupedByType.entrySet()) {
                String type = entry.getKey();
                List<QueryStockQuote> groupList = entry.getValue();

                // 构建批量查询参数
                StringBuilder thirdPartyCodesBuilder = new StringBuilder();
                Map<String, QueryStockQuote> thirdPartyCodeMap = new HashMap<>();

                for (QueryStockQuote queryStockQuote : groupList) {
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
                    continue;
                }

                // 构建批量请求URL
                String url = String.format(stockCodeMappingConfig.getQuoteSummaryUrl(), stockCodeMappingConfig.getKey(), stockCodeMappingConfig.getId(), thirdPartyCodesBuilder, "Charts",type);
                // 创建HTTP头
                HttpHeaders headers = new HttpHeaders();
                headers.set("Accept", "application/json");

                // 发送请求
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                // 解析响应
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                // 设置当前时间为msnDataTime和itickDataTime
                LocalDateTime currentTime = LocalDateTime.now();

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
                                    StockChartQuote  cachedChartQuote = convertToStockChartQuote(quoteData, queryStockQuote);
                                    // 设置msnDataTime
                                    cachedChartQuote.setMsnDataChartTime(currentTime);
                                    // 构建缓存键
                                    String redisKey = getRedisKey(queryStockQuote);
                                    // 尝试从缓存获取
                                    StockQuote cachedQuote = redisService.getCacheObject(redisKey);
                                    cachedQuote.setStockChartQuote(cachedChartQuote);
                                    // 添加到结果列表
                                    resultList.add(cachedQuote);
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


    /**
     * 将MSN API响应的图表数据转换为StockChartQuote对象
     *
     * @param chartNode API响应的图表数据
     * @param queryStockQuote 查询参数
     * @return StockChartQuote对象
     */
    private StockChartQuote convertToStockChartQuote(JsonNode chartNode, QueryStockQuote queryStockQuote) {
        StockChartQuote stockChartQuote = new StockChartQuote();
        stockChartQuote.setStockCode(queryStockQuote.getStockCode());
        stockChartQuote.setMarketCode(queryStockQuote.getMarketCode());

        if (chartNode != null) {
            // 从chart节点提取数据
            JsonNode chartData = chartNode.get("chart");
            if (chartData != null) {
                // 从series节点提取图表数据
                JsonNode series = chartData.get("series");
                if (series != null) {
                    // 提取各种价格数组
                    stockChartQuote.setOpenPrices(parseBigDecimalArray(series.get("openPrices")));
                    stockChartQuote.setPrices(parseBigDecimalArray(series.get("prices")));
                    stockChartQuote.setPricesHigh(parseBigDecimalArray(series.get("pricesHigh")));
                    stockChartQuote.setPricesLow(parseBigDecimalArray(series.get("pricesLow")));
                    stockChartQuote.setVolumes(parseBigDecimalArray(series.get("volumes")));

                    // 提取时间戳数组
                    stockChartQuote.setTimeStamps(parseStringArray(series.get("timeStamps")));

                    // 提取最高价和最低价
                    stockChartQuote.setPriceHigh(getBigDecimalFromNode(series, "priceHigh"));
                    stockChartQuote.setPriceLow(getBigDecimalFromNode(series, "priceLow"));
                }
            }
        }

        return stockChartQuote;
    }

    /**
     * 解析BigDecimal数组
     */
    private BigDecimal[] parseBigDecimalArray(JsonNode arrayNode) {
        if (arrayNode != null && arrayNode.isArray()) {
            List<BigDecimal> values = new ArrayList<>();
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode element = arrayNode.get(i);
                if (element != null && !element.isNull() && element.isNumber()) {
                    values.add(BigDecimal.valueOf(element.asDouble()));
                } else {
                    values.add(null);
                }
            }
            return values.toArray(new BigDecimal[0]);
        }
        return new BigDecimal[0];
    }

    /**
     * 解析字符串数组
     */
    private String[] parseStringArray(JsonNode arrayNode) {
        if (arrayNode != null && arrayNode.isArray()) {
            List<String> values = new ArrayList<>();
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode element = arrayNode.get(i);
                if (element != null && !element.isNull() && element.isTextual()) {
                    values.add(element.asText());
                } else {
                    values.add(null);
                }
            }
            return values.toArray(new String[0]);
        }
        return new String[0];
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
