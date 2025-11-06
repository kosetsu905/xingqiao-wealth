package com.xingqiao.order.quote.impl;

import com.longport.Config;
import com.longport.quote.QuoteContext;
import com.longport.quote.SecurityQuote;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.redis.service.RedisService;
import com.xingqiao.order.config.StockCodeMappingConfig;
import com.xingqiao.order.config.StockQueryMappingConfig;
import com.xingqiao.order.quote.QuoteApiStrategy;
import com.xingqiao.order.utils.TimeRangeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.xingqiao.order.config.TradeConstants.getRedisKey;

/**
 * LongPort行情API策略实现类
 * 实现QuoteApiStrategy接口，使用LongPort SDK提供行情服务
 * 集成Spring Boot生命周期管理，支持行情订阅和实时数据获取
 *
 * @author Java后端架构师
 */
@Slf4j
@Component
public class LongportApiStrategy implements QuoteApiStrategy {

    // 配置属性，从Spring配置中注入
    @Autowired
    private Config config;
    // LongPort行情上下文
    @Autowired
    private QuoteContext quoteContext;
    @Autowired
    private  RedisService redisService;
    @Autowired
    private  StockCodeMappingConfig stockCodeMappingConfig;
    @Autowired
    private  TimeRangeUtil timeRangeUtil;
    @Autowired
    private  StockQueryMappingConfig stockQueryMappingConfig;

    /**
     * 获取单只股票实时行情（不使用缓存）
     * 直接从LongPort API获取最新行情
     *
     * @param queryStockQuote 查询条件
     * @return 股票行情信息
     */
    @Override
    public R<StockQuote> getStockCurrentQuote(QueryStockQuote queryStockQuote) {
        if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
            return R.fail("股票代码不能为空");
        }

        try {
            String stockCode = queryStockQuote.getStockCode();
            String marketCode = queryStockQuote.getMarketCode();
            String fullSymbol = stockCode + "." + marketCode;


            // 使用新实现的getQuoteSync方法获取行情
            SecurityQuote[] quoteArray = getQuoteSync(new String[] { fullSymbol });

            if (quoteArray.length == 0) {
                log.warn("未获取到股票 {} 的行情数据", fullSymbol);
                return R.fail("未获取到行情数据");
            }

            // 转换为系统的StockQuote对象
            SecurityQuote securityQuote = quoteArray[0];
            StockQuote stockQuote = convertToStockQuote(securityQuote, queryStockQuote);

            log.info("获取股票实时行情成功: {} -> {}", fullSymbol, stockQuote.getCurrentPrice());
            return R.ok(stockQuote);

        } catch (Exception e) {
            log.error("获取股票实时行情失败", e);
            return R.fail("获取行情失败: " + e.getMessage());
        }
    }

    /**
     * 将LongPort的SecurityQuote对象转换为系统的StockQuote对象
     * 根据SecurityQuote的实际字段结构进行正确映射
     *
     * @param securityQuote LongPort行情对象
     * @param queryStockQuote 查询条件
     * @return 系统StockQuote对象
     */
    private StockQuote convertToStockQuote(SecurityQuote securityQuote, QueryStockQuote queryStockQuote) {
        if (securityQuote == null) {
            return null;
        }

        StockQuote stockQuote = new StockQuote();

        // 设置股票基本信息
        stockQuote.setProductCode(queryStockQuote.getProductCode());
        stockQuote.setStockCode(queryStockQuote.getStockCode());
        stockQuote.setMarketCode(queryStockQuote.getMarketCode());

        // 设置价格信息 - 使用lastDone代替lastPrice
        stockQuote.setCurrentPrice(securityQuote.getLastDone()); // 设置当前价格
        stockQuote.setOpenPrice(securityQuote.getOpen());
        stockQuote.setHighPrice(securityQuote.getHigh());
        stockQuote.setLowPrice(securityQuote.getLow());
        stockQuote.setPrevClosePrice(securityQuote.getPrevClose());
        stockQuote.setClosePrice(securityQuote.getLastDone());

        // 计算涨跌幅
        BigDecimal lastDone = securityQuote.getLastDone();
        BigDecimal prevClose = securityQuote.getPrevClose();
        if (lastDone != null && prevClose != null && prevClose.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal change = lastDone.subtract(prevClose);
            BigDecimal changePercent = change.divide(prevClose, 6, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            stockQuote.setPriceChange(change);
            stockQuote.setPriceChangePercent(changePercent);
        }

        // 设置时间戳
        OffsetDateTime timestamp = securityQuote.getTimestamp();
        if (timestamp != null) {
            stockQuote.setTimeLastUpdated(timestamp.toLocalDateTime());
        }

        // 设置成交量和成交额
        stockQuote.setVolume(securityQuote.getVolume());
        stockQuote.setAmount(securityQuote.getTurnover());

        return stockQuote;
    }


    /**
     * 获取单只股票行情（支持缓存）
     * 优先从缓存获取，如果缓存未命中则调用实时行情方法
     *
     * @param queryStockQuote 查询条件
     * @return 股票行情信息
     */
    @Override
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
            return R.fail("股票代码不能为空");
        }

        try {
            String stockCode = queryStockQuote.getStockCode();
            String marketCode = queryStockQuote.getMarketCode();
            String fullSymbol = stockCode + "." + marketCode;
            log.info("fullSymbol{}",fullSymbol);

            // 构建Redis Hash缓存键
            String redisKey = getRedisKey(queryStockQuote);
            // 尝试从Redis Hash中获取缓存数据
            StockQuote cachedQuote = redisService.getCacheObject(redisKey);
            LocalDateTime currentTime = LocalDateTime.now();
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

                // 行情时间内，继续检查缓存是否过期（2分钟）
                if (cachedQuote.getLongPortDataTime() != null) {
                    // 计算缓存时间与当前时间的差值（2分钟）
                    long cacheDiffMinutes = java.time.Duration.between(cachedQuote.getLongPortDataTime(), currentTime).toMinutes();
                    if (cacheDiffMinutes < stockCodeMappingConfig.getFrequency()) {
                        // 缓存未过期（2分钟内），返回缓存数据
                        log.info("从Hash缓存获取股票行情（缓存未过期）：{}", queryStockQuote.getStockCode());
                        return R.restResult(cachedQuote, 201, "获取股票行情成功");
                    } else {
                        log.info("缓存已过期（超过2分钟），重新获取股票行情：{}", queryStockQuote.getStockCode());
                    }
                }
            }

            // 缓存未命中时调用实时行情方法
            R<StockQuote> realTimeResult = getStockCurrentQuote(queryStockQuote);
            if (R.isSuccess(realTimeResult)&&realTimeResult.getData()!=null){
                StockQuote stockQuote=realTimeResult.getData();
                stockQuote.setLongPortDataTime(currentTime);
                //保存获取的股票数据到Redis
                redisService.setCacheObject(redisKey, stockQuote);
            }
            log.info("获取股票行情成功并保存到Hash缓存：{}", queryStockQuote.getStockCode());
            return realTimeResult;

        } catch (Exception e) {
            log.error("获取股票行情失败", e);
            return R.fail("获取股票行情失败: " + e.getMessage());
        }
    }

    /**
     * 批量获取股票行情
     * 使用异步并发方式处理多个股票行情查询，提高性能
     *
     * @param list 查询条件列表
     * @return 股票行情列表
     */
    @Override
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list) {
        if (CollectionUtils.isEmpty(list)) {
            log.info("批量获取行情时查询列表为空，返回空结果");
            return R.ok(new ArrayList<>());
        }

        log.info("开始批量获取股票行情，数量: {}", list.size());

        if (!stockQueryMappingConfig.isLongPortGetStockQuoteListSwitch()){
            return R.fail("获取股票行情失败：行情API未启用");
        }
        log.info("批量获取股票行情，数量：{}", list.size());

        // 参数验证
        if (list.isEmpty()) {
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
                    if (cachedQuote.getLongPortDataTime() != null) {
                        long diffMinutes = java.time.Duration.between(cachedQuote.getLongPortDataTime(), currentTime).toMinutes();
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
                try {
                    // 优化1：直接创建指定大小的数组，避免中间List
                    String[] fullSymbols = new String[needQueryList.size()];
                    for (int i = 0; i < needQueryList.size(); i++) {
                        QueryStockQuote queryStockQuote = needQueryList.get(i);
                        fullSymbols[i] = queryStockQuote.getStockCode() + "." + queryStockQuote.getMarketCode();
                    }

                    // 使用新实现的getQuoteSync方法获取行情
                    SecurityQuote[] quoteArray = getQuoteSync(fullSymbols);

                    if (quoteArray.length == 0) {
                        log.warn("未获取到股票 {} 的行情数据", Arrays.toString(fullSymbols));
                        return R.fail("未获取到行情数据");
                    }

                    // 优化2：预先构建映射关系，避免在循环内重复过滤
                    Map<String, QueryStockQuote> symbolToQueryMap = needQueryList.stream()
                            .collect(Collectors.toMap(
                                    item -> item.getMarketCode() + "." + item.getStockCode(),
                                    Function.identity()
                            ));

                    // 优化3：使用增强for循环提高可读性
                    for (SecurityQuote securityQuote : quoteArray) {
                        String symbolCode = securityQuote.getSymbol();
                        QueryStockQuote matchingQuery = symbolToQueryMap.get(symbolCode);

                        if (matchingQuery != null) {
                            StockQuote stockQuote = convertToStockQuote(securityQuote, matchingQuery);
                            stockQuote.setLongPortDataTime(currentTime);

                            // 保存到缓存
                            String hashKey = getRedisKey(matchingQuery);
                            redisService.setCacheObject(hashKey, stockQuote);
                            resultList.add(stockQuote);

                            log.info("获取股票实时行情成功: {} -> {}", symbolCode, securityQuote.getOpen());
                        }
                    }

                    return R.ok(resultList);
                } catch (Exception e) {
                    log.error("获取股票实时行情失败", e);
                    return R.fail("获取行情失败: " + e.getMessage());
                }
            }
            log.info("批量获取股票行情完成，成功数量: {}", resultList.size());
            return R.ok(resultList);

        } catch (Exception e) {
            log.error("批量获取股票行情失败", e);
            return R.fail("批量获取股票行情失败: " + e.getMessage());
        }
    }

    /**
     * 批量获取股票图表行情
     * 支持获取K线数据，可用于绘制股票图表
     *
     * @param list 查询条件列表，包含时间范围、K线周期等参数
     * @return 股票图表行情列表
     */
    @Override
    public R<List<StockQuote>> getStockQuoteChartList(List<QueryStockQuote> list) {
        if (CollectionUtils.isEmpty(list)) {
            log.info("批量获取图表行情时查询列表为空，返回空结果");
            return R.ok(new ArrayList<>());
        }

        log.info("开始批量获取股票图表行情，数量: {}", list.size());


        try {

            List<StockQuote> chartDataList = new ArrayList<>();
            //todo

            log.info("批量获取股票图表行情完成，成功数量: {}", chartDataList.size());
            return R.ok(chartDataList);

        } catch (Exception e) {
            log.error("批量获取股票图表行情失败", e);
            return R.fail("批量获取股票图表行情失败: " + e.getMessage());
        }
    }


    /**
     * 获取策略名称
     *
     * @return 策略名称
     */
    @Override
    public String getStrategyName() {
        return "longPort";
    }

    /**
     * 同步获取股票行情数据
     * 调用LongPort SDK获取指定股票的实时行情
     *
     * @param symbols 股票代码数组，格式为"股票代码.市场代码"
     * @return 股票行情数组
     * @throws Exception 如果获取行情失败
     */
    private SecurityQuote[] getQuoteSync(String[] symbols) throws Exception {
        if (symbols == null || symbols.length == 0) {
            log.warn("获取行情时股票代码数组为空");
            return new SecurityQuote[0];
        }

        if (quoteContext == null) {
            throw new IllegalStateException("行情上下文未初始化");
        }

        log.info("开始同步获取股票行情，数量: {}", symbols.length);

        // 调用LongPort SDK获取行情数据
        // 注意：这里假设quoteContext.getQuote()返回CompletableFuture<SecurityQuote[]>
        SecurityQuote[] quotes = quoteContext.getQuote(symbols).get();

        if (quotes == null) {
            log.warn("行情数据返回为空");
            return new SecurityQuote[0];
        }

        log.info("同步获取股票行情完成，成功获取: {}", quotes.length);
        return quotes;
    }

    /**
     * 异步获取股票行情数据
     * 基于CompletableFuture实现异步行情获取
     *
     * @param symbols 股票代码数组
     * @return CompletableFuture包装的行情数组
     */
    private CompletableFuture<SecurityQuote[]> getQuote(String[] symbols) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getQuoteSync(symbols);
            } catch (Exception e) {
                log.error("异步获取行情失败", e);
                throw new RuntimeException("获取行情失败", e);
            }
        });
    }
    
    /**
     * 销毁LongPort行情服务
     * 在Spring容器关闭时自动执行，确保资源正确释放
     */
    @PreDestroy
    public void destroy() {
        if (quoteContext != null) {
            try {
                quoteContext.close();
                log.info("LongPort行情服务已关闭");
            } catch (Exception e) {
                log.error("关闭LongPort行情服务失败", e);
            }
        }
    }
}
