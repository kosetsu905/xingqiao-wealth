package com.xingqiao.order.controller;

import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.order.service.TradeApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 交易服务Controller
 * 实现交易和行情相关的RESTful API
 * 
 * @author xingqiao
 */
@RestController
@RequestMapping("/api/trade")
@Slf4j
public class TradeController extends BaseController{
    
    @Autowired
    private TradeApiService tradeApiService;

    
    /**
     * 查询交易详情
     * @param tradeId 交易ID
     * @return 交易详情
     */
    @GetMapping("/detail")
    public R getTradeDetail(@RequestParam("tradeId") Long tradeId) {
        try {
            log.info("查询交易详情: tradeId={}", tradeId);
            return tradeApiService.getTradeDetail(tradeId);
        } catch (Exception e) {
            log.error("查询交易详情失败: {}", e.getMessage(), e);
            return R.fail("查询交易详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建交易订单
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    @PostMapping("/create")
    public R createTrade(@RequestBody TradeRequest tradeRequest) {
        try {
            log.info("创建交易订单: request={}", tradeRequest);
            return tradeApiService.createTrade(tradeRequest);
        } catch (Exception e) {
            log.error("创建交易订单失败: {}", e.getMessage(), e);
            return R.fail("创建交易订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 取消交易订单
     * @param tradeId 交易ID
     * @return 取消结果
     */
    @PostMapping("/cancel")
    public R cancelTrade(@RequestParam("tradeId") Long tradeId) {
        try {
            log.info("取消交易订单: tradeId={}", tradeId);
            return tradeApiService.cancelTrade(tradeId);
        } catch (Exception e) {
            log.error("取消交易订单失败: {}", e.getMessage(), e);
            return R.fail("取消交易订单失败: " + e.getMessage());
        }
    }

}