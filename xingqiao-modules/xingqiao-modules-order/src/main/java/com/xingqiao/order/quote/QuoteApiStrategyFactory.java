package com.xingqiao.order.quote;

import com.alibaba.fastjson.JSON;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.common.core.domain.R;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 行情API策略工厂类
 * 负责管理所有的QuoteApiStrategy实现，并根据策略名称获取对应的策略
 * 
 * @author xingqiao
 * @date 2025-09-23
 */
@Slf4j
@Component
public class QuoteApiStrategyFactory {

    @Autowired
    private Set<QuoteApiStrategy> quoteApiStrategies;

    private final Map<String, QuoteApiStrategy> strategyMap = new HashMap<>();

    /**
     * -- GETTER --
     *  获取默认的行情API策略
     *
     * @return 默认行情API策略实现
     */
    @Getter
    private QuoteApiStrategy defaultStrategy;

    /**
     * 初始化策略工厂
     * 将所有的QuoteApiStrategy实现放入Map中，并设置默认策略
     */
    @PostConstruct
    public void init() {
        for (QuoteApiStrategy strategy : quoteApiStrategies) {
            String strategyName = strategy.getStrategyName();
            // 设置默认策略
            if ("default".equals(strategyName)) {
                defaultStrategy = strategy;
            }else{
                strategyMap.put(strategyName, strategy);
            }
        }
    }
    /**
     * 获取历史行情数据
     * 遍历执行所有策略，若返回R为200且有值则中断执行并返回数据，若所有执行完毕仍无200则返回默认策略的数据
     * @param queryStockQuote 股票查询条件
     * @return 历史行情列表结果
     */
    public R<List<StockQuote>> getStockQuoteHistory(QueryStockQuote queryStockQuote) {
        log.info("开始获取历史行情数据: {}", queryStockQuote);
        
        // 参数验证
        if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
            log.warn("股票代码不能为空");
            return R.fail("股票代码不能为空");
        }
        
        // 遍历所有策略
        for (QuoteApiStrategy strategy : quoteApiStrategies) {
            try {
                log.debug("尝试使用策略: {} 获取历史行情数据", strategy.getStrategyName());
                R<List<StockQuote>> result = strategy.getStockQuoteHistory(queryStockQuote);
                
                // 判断返回结果是否符合要求：状态码为200且有值
                if (result != null && result.getCode() == 200 && result.getData() != null && !result.getData().isEmpty()) {
                    log.info("策略: {} 获取历史行情数据成功，返回结果", strategy.getStrategyName());
                    return result; // 中断执行并返回数据
                }
                log.debug("策略: {} 返回结果不符合要求或为空，继续尝试其他策略", strategy.getStrategyName());
            } catch (Exception e) {
                log.error("使用策略: {} 获取历史行情数据时发生异常", strategy.getStrategyName(), e);
                // 发生异常时继续尝试下一个策略
                continue;
            }
        }
        
        // 所有策略都执行完毕且没有返回状态码为200的结果
        // 使用默认策略获取数据
        log.warn("所有策略均未成功获取历史行情数据，尝试使用默认策略获取数据");
        try {
            if (defaultStrategy != null) {
                R<List<StockQuote>> defaultResult = defaultStrategy.getStockQuoteHistory(queryStockQuote);
                if (defaultResult != null) {
                    log.info("从默认策略获取历史行情数据成功");
                    return defaultResult;
                }
            } else {
                log.error("默认策略未初始化，无法获取历史行情数据");
            }
        } catch (Exception e) {
            log.error("尝试从默认策略获取历史行情数据时发生异常", e);
        }
        
        // 如果连默认策略都失败，则返回空结果
        log.error("无法获取任何历史行情数据，返回空结果");
        return R.ok(Collections.emptyList());
    }
    

    /**
     * 根据策略名称获取行情API策略
     * @param strategyName 策略名称
     * @return 行情API策略实现
     */
    public QuoteApiStrategy getStrategy(String strategyName) {
        log.debug("尝试获取行情API策略: {}", strategyName);
        
        if (strategyName != null && strategyMap.containsKey(strategyName)) {
            QuoteApiStrategy strategy = strategyMap.get(strategyName);
            log.debug("成功获取策略: {}, 策略实现类: {}", strategyName, strategy.getClass().getSimpleName());
            return strategy;
        }
        
        // 如果找不到指定的策略，记录警告日志并返回默认策略
        if (strategyName != null) {
            log.warn("找不到指定的策略: {}, 将使用默认策略", strategyName);
        } else {
            log.debug("未指定策略名称，将使用默认策略");
        }
        
        if (defaultStrategy != null) {
            log.debug("返回默认策略，实现类: {}", defaultStrategy.getClass().getSimpleName());
        } else {
            log.error("默认策略未初始化，返回null");
        }
        
        return defaultStrategy;
    }
    
    /**
     * 批量获取股票行情
     * 遍历执行所有策略，若返回R为200且有值则中断执行并返回数据，若所有执行完毕仍无200则返回缓存中历史数据
     * @param queryStockQuoteList 股票查询列表
     * @return 股票行情列表结果
     */
    public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> queryStockQuoteList) {
        log.info("开始批量获取股票行情，请求数量: {}", queryStockQuoteList != null ? queryStockQuoteList.size() : 0);
        
        // 参数验证
        if (queryStockQuoteList == null || queryStockQuoteList.isEmpty()) {
            log.warn("股票代码列表不能为空");
            return R.fail("股票代码列表不能为空");
        }
        
        // 遍历所有策略
        for (QuoteApiStrategy strategy : quoteApiStrategies) {
            try {
                log.debug("尝试使用策略: {} 获取行情数据", strategy.getStrategyName());
                R<List<StockQuote>> result = strategy.getStockQuoteList(queryStockQuoteList);
                
                // 判断返回结果是否符合要求：状态码为200且有值
                if (result != null && result.getCode() == 200 && result.getData() != null
                        && !result.getData().isEmpty()) {
                    log.info("策略: {} 获取行情数据成功，返回结果", strategy.getStrategyName());
                    return result; // 中断执行并返回数据
                }
                log.debug("策略: {} 返回结果不符合要求或为空，继续尝试其他策略", strategy.getStrategyName());
            } catch (Exception e) {
                    log.error("使用策略: {} 获取行情数据时发生异常", strategy.getStrategyName(), e);
                }
        }
        
        // 所有策略都执行完毕且没有返回状态码为200的结果
        // 使用默认策略获取缓存中的历史数据
        log.warn("所有策略均未成功获取行情数据，尝试从缓存获取历史数据");
        try {
            R<List<StockQuote>> defaultResult = defaultStrategy.getStockQuoteList(queryStockQuoteList);
            if (defaultResult != null && defaultResult.getData() != null) {
                log.info("从默认策略获取历史缓存数据成功");
                return defaultResult;
            }
        } catch (Exception e) {
            log.error("尝试从默认策略获取历史数据时发生异常", e);
        }
        
        // 如果连默认策略都失败，则返回空结果
        log.error("无法获取任何行情数据，返回空结果");
        return R.ok(Collections.emptyList());
    }

    /**
     * 获取单只股票行情
     * 遍历执行所有策略，若返回R为200且有值则中断执行并返回数据，若所有执行完毕仍无200则返回默认策略的数据
     * @param queryStockQuote 股票查询条件
     * @return 股票行情结果
     */
    public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote) {
        log.info("开始获取股票行情: {}", queryStockQuote);
        
        // 参数验证
        if (queryStockQuote == null || queryStockQuote.getStockCode() == null) {
            log.warn("股票代码不能为空");
            return R.fail("股票代码不能为空");
        }
        
        // 遍历所有策略
        for (QuoteApiStrategy strategy : quoteApiStrategies) {
            try {
                log.debug("尝试使用策略: {} 获取行情数据", strategy.getStrategyName());
                R<StockQuote> result = strategy.getStockQuote(queryStockQuote);
                
                // 判断返回结果是否符合要求：状态码为200且有值
                if (result != null && result.getCode() == 200 && result.getData() != null) {
                    log.info("策略: {} 获取行情数据成功，返回结果", strategy.getStrategyName());
                    return result; // 中断执行并返回数据
                }
                log.debug("策略: {} 返回结果不符合要求或为空，继续尝试其他策略", strategy.getStrategyName());
            } catch (Exception e) {
                log.error("使用策略: {} 获取行情数据时发生异常", strategy.getStrategyName(), e);
                // 发生异常时继续尝试下一个策略
                continue;
            }
        }
        
        // 所有策略都执行完毕且没有返回状态码为200的结果
        // 使用默认策略获取数据
        log.warn("所有策略均未成功获取行情数据，尝试使用默认策略获取数据");
        try {
            if (defaultStrategy != null) {
                R<StockQuote> defaultResult = defaultStrategy.getStockQuote(queryStockQuote);
                if (defaultResult != null) {
                    log.info("从默认策略获取行情数据成功");
                    return defaultResult;
                }
            } else {
                log.error("默认策略未初始化，无法获取行情数据");
            }
        } catch (Exception e) {
            log.error("尝试从默认策略获取数据时发生异常", e);
        }
        
        // 如果连默认策略都失败，则返回空结果
        log.error("无法获取任何行情数据，返回失败结果");
        return R.fail("无法获取股票行情数据");
    }
}