package com.xingqiao.order.controller;

import com.xingqiao.order.service.AlphaVantageService;
import com.xingqiao.common.core.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock")
public class StockDataController {

    private final AlphaVantageService alphaVantageService;

    // 构造函数注入
    public StockDataController(AlphaVantageService alphaVantageService) {
        this.alphaVantageService = alphaVantageService;
    }

    /**
     * 获取股票数据（包含公司名称）
     * 例子： /stock/data/AAPL?interval=5min
     */
    @GetMapping("/data/{symbol}")
    public AjaxResult getStockData(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "5min") String interval) {
        try {
            Map<String, Object> stockInfo = alphaVantageService.getStockDataWithCompany(symbol, interval);

            // Java 8 安全提取 preMarket 最新价
            Object preMarketObj = stockInfo.get("preMarketData");
            if (preMarketObj != null && preMarketObj instanceof List) {
                List preMarketList = (List) preMarketObj;
                if (!preMarketList.isEmpty()) {
                    Object last = preMarketList.get(preMarketList.size() - 1);
                    if (last != null && last instanceof Map) {
                        stockInfo.put("preMarketLatest", last);
                    }
                }
            }

            return AjaxResult.success(stockInfo);
        } catch (Exception e) {
            return AjaxResult.error("获取股票数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/global")
    public AjaxResult getGlobalMarkets() {
        try {
            List<Map<String, Object>> markets = alphaVantageService.getGlobalMarkets();
            return AjaxResult.success(markets);
        } catch (Exception e) {
            return AjaxResult.error("获取全球市场失败: " + e.getMessage());
        }
    }

}
