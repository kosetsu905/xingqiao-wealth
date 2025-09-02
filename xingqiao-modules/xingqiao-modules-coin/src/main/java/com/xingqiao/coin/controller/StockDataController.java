package com.xingqiao.coin.controller;

import com.xingqiao.coin.service.AlphaVantageService;
import com.xingqiao.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockDataController {

    @Autowired
    private AlphaVantageService alphaVantageService;

    /**
     * 获取股票数据
     * 例子： /stock/data/AAPL?interval=5min
     */
    @GetMapping("/data/{symbol}")
    public AjaxResult getStockData(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "5min") String interval) {
        try {
            return AjaxResult.success(alphaVantageService.getStockData(symbol, interval));
        } catch (Exception e) {
            return AjaxResult.error("获取股票数据失败: " + e.getMessage());
        }
    }
}
