package com.xingqiao.order.controller;

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
@RequestMapping("/quote")
@Slf4j
public class QuoteController extends BaseController {

    @Autowired
    private QuoteApiService quoteApiService;
    /**
     * 获取单只股票行情
     * @return 股票行情信息
     */
    @PostMapping("/getStockQuote")
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
    @PostMapping("/getStockQuoteList")
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
     * 批量获取区间行情数据
     */
    @PostMapping("/getStockQuoteChartList")
    public R<List<StockQuote>> getStockQuoteChartList(@RequestBody List<QueryStockQuote> list) {
        try {
            log.info("批量获取区间行情数据: size={}", list != null ? list.size() : 0);
            return quoteApiService.getStockQuoteChartList(list);
        } catch (Exception e) {
            log.error("批量获取区间行情数据失败: {}", e.getMessage(), e);
            return R.fail("批量获取区间行情数据失败: " + e.getMessage());
        }
    }

}
