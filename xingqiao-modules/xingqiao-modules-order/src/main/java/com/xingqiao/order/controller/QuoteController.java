package com.xingqiao.order.controller;

import com.alibaba.fastjson.JSON;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.QueryStockQuoteList;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.order.service.QuoteApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/quote")
@Slf4j
public class QuoteController extends BaseController {

    @Autowired
    private QuoteApiService quoteApiService;
    /**
     * 获取单只股票行情
     * @return 股票行情信息
     */
    @PostMapping("/get")
    public R<StockQuote> getStockQuote(@RequestBody QueryStockQuote queryStockQuote) {
        try {
            log.info("获取单只股票行情: request={}", queryStockQuote);
            return quoteApiService.getStockQuote(queryStockQuote);
        } catch (Exception e) {
            log.error("获取股票行情失败: {}", e.getMessage(), e);
            return R.fail("获取股票行情失败: " + e.getMessage());
        }
    }

    /**
     * 批量获取股票行情
     * @return 股票行情列表
     */
    @PostMapping("/list")
    public R<List<StockQuote>> getStockQuoteList(@RequestBody List<QueryStockQuote> list) {
        try {
            log.info("批量获取股票行情: size={}", list != null ? list.size() : 0);
            return quoteApiService.getStockQuoteList(list);
        } catch (Exception e) {
            log.error("批量获取股票行情失败: {}", e.getMessage(), e);
            return R.fail("批量获取股票行情失败: " + e.getMessage());
        }
    }

    /**
     * 获取历史行情数据
     * @return 历史行情列表
     */
    @PostMapping("/history")
    public R<List<StockQuote>> getStockQuoteHistory(@RequestBody QueryStockQuote queryStockQuote) {
        try {
            log.info("获取历史行情数据: request={}", queryStockQuote);
            return quoteApiService.getStockQuoteHistory(queryStockQuote);
        } catch (Exception e) {
            log.error("获取历史行情失败: {}", e.getMessage(), e);
            return R.fail("获取历史行情失败: " + e.getMessage());
        }
    }

    /**
     * 订阅股票行情
     * @return 订阅结果
     */
    @PostMapping("/subscribe")
    public R subscribeStockQuote(@RequestBody QueryStockQuoteList queryStockQuoteList) {
        try {
            log.info("订阅股票行情: request={}", queryStockQuoteList.toString());
            Long userId = SecurityUtils.getUserId();
            queryStockQuoteList.setUserId(userId);
            return quoteApiService.subscribeStockQuote(queryStockQuoteList);
        } catch (Exception e) {
            log.error("订阅股票行情失败: {}", e.getMessage(), e);
            return R.fail("订阅股票行情失败: " + e.getMessage());
        }
    }

    /**
     * 取消订阅股票行情
     * @return 取消订阅结果
     */
    @PostMapping("/unsubscribe")
    public R unsubscribeStockQuote(@RequestBody QueryStockQuoteList queryStockQuoteList) {
        try {
            log.info("取消订阅股票行情: request={}", queryStockQuoteList.toString());
            Long userId = SecurityUtils.getUserId();
            queryStockQuoteList.setUserId(userId);
            return quoteApiService.unsubscribeStockQuote(queryStockQuoteList);
        } catch (Exception e) {
            log.error("取消订阅股票行情失败: {}", e.getMessage(), e);
            return R.fail("取消订阅股票行情失败: " + e.getMessage());
        }
    }
}
