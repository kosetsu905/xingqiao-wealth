package com.xingqiao.order.quote;

import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.order.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 行情API策略工厂测试类
 * 测试获取单个股票行情和批量股票行情的功能
 * 
 * @author xingqiao
 * @date 2025-09-23
 */
@Slf4j
public class QuoteApiStrategyFactoryTest extends BaseTest {

    @Autowired
    private QuoteApiStrategyFactory quoteApiStrategyFactory;



    /**
     * 测试获取单个股票行情
     */
    @Test
    public void testGetStockQuote() {
        log.info("开始测试获取单个股票行情");
        
        // 创建测试用的股票查询对象
        QueryStockQuote queryStockQuote = new QueryStockQuote();
        queryStockQuote.setStockCode("000001");
        queryStockQuote.setMarketCode("SH");
        queryStockQuote.setProductCode("indices");

        // 调用方法获取股票行情
        R<StockQuote> result = quoteApiStrategyFactory.getStockQuote(queryStockQuote);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为空");
        log.info("获取单个股票行情结果: 状态码={}, 消息={}", result.getCode(), result.getMsg());
        
        // 如果有数据，打印股票信息
        if (result.getData() != null) {
            log.info("股票代码: {}, 股票名称: {}, 最新价格: {}", 
                    result.getData().getStockCode(), 
                    result.getData().getStockName(), 
                    result.getData().getCurrentPrice());
        }
    }

    /**
     * 测试获取批量股票行情
     */
    @Test
    public void testGetStockQuoteList() {
        log.info("开始测试获取批量股票行情");
        
        // 创建多个测试用的股票查询对象
        QueryStockQuote query1 = new QueryStockQuote();
        query1.setStockCode("000001");
        query1.setMarketCode("SH");
        query1.setProductCode("indices");

        QueryStockQuote query2 = new QueryStockQuote();
        query2.setStockCode("399001");
        query2.setMarketCode("SZ");
        query2.setProductCode("indices");


        QueryStockQuote query3 = new QueryStockQuote();
        query3.setStockCode("IBOV");
        query3.setMarketCode("US");
        query3.setProductCode("indices");

        List<QueryStockQuote> queryList = Arrays.asList(query1, query2,query3);
        
        // 调用方法获取批量股票行情
        R<List<StockQuote>> result = quoteApiStrategyFactory.getStockQuoteList(queryList);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为空");
        log.info("获取批量股票行情结果: 状态码={}, 消息={}", result.getCode(), result.getMsg());
        
        // 如果有数据，打印股票信息
        if (result.getData() != null) {
            log.info("获取到 {} 只股票行情数据", result.getData().size());
            for (StockQuote quote : result.getData()) {
                log.info("股票代码: {}, 股票名称: {}, 最新价格: {}", 
                        quote.getStockCode(), 
                        quote.getStockName(), 
                        quote.getCurrentPrice());
            }
        }
    }
    
    /**
     * 测试参数验证 - 空股票代码
     */
    @Test
    public void testGetStockQuoteWithNullCode() {
        log.info("开始测试参数验证 - 空股票代码");
        
        QueryStockQuote queryStockQuote = new QueryStockQuote();
        // 不设置股票代码
        
        R<StockQuote> result = quoteApiStrategyFactory.getStockQuote(queryStockQuote);
        
        assertNotNull(result, "返回结果不应为空");
        assertFalse(R.isSuccess(result), "空股票代码应返回失败");
        log.info("空股票代码测试结果: 状态码={}, 消息={}", result.getCode(), result.getMsg());
    }
    
    /**
     * 测试参数验证 - 空列表
     */
    @Test
    public void testGetStockQuoteListWithEmptyList() {
        log.info("开始测试参数验证 - 空列表");
        
        List<QueryStockQuote> emptyList = Collections.emptyList();
        
        R<List<StockQuote>> result = quoteApiStrategyFactory.getStockQuoteList(emptyList);
        
        assertNotNull(result, "返回结果不应为空");
        assertFalse(R.isSuccess(result), "空列表应返回失败");
        log.info("空列表测试结果: 状态码={}, 消息={}", result.getCode(), result.getMsg());
    }
}