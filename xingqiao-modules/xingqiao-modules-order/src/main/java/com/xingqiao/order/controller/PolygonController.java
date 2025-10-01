package com.xingqiao.order.controller;

import com.xingqiao.order.service.PolygonService;
import com.xingqiao.common.core.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/market")
public class PolygonController {

    private final PolygonService polygonService;

    public PolygonController(PolygonService polygonService) {
        this.polygonService = polygonService;
    }

    /**
     * 获取单个市场行情
     * 例子： /market/data/I:DJI
     */
    @GetMapping("/data/{stockCode}")
    public AjaxResult getMarketData(@PathVariable String stockCode) {
        try {
            Map<String, Object> data = polygonService.getMarketData(stockCode);
            return AjaxResult.success(data);
        } catch (Exception e) {
            return AjaxResult.error("获取市场行情失败: " + e.getMessage());
        }
    }
}
