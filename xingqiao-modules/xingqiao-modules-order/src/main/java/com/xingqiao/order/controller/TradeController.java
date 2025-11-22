package com.xingqiao.order.controller;

import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.common.security.utils.SecurityUtils;
import com.xingqiao.order.service.TradeApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 交易服务Controller
 * 实现交易相关的RESTful API
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
     * 创建交易订单
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    @PostMapping("/create")
    public R createTrade(@RequestBody TradeRequest tradeRequest) {
        try {
            // 从token获取当前登录用户ID
            Long userId = SecurityUtils.getUserId();
            if (userId == null || userId == 0) {
                return R.fail("用户未登录或登录已过期");
            }
            
            log.info("创建交易订单: userId={}, request={}", userId, tradeRequest);
            return tradeApiService.createTrade(userId, tradeRequest);
        } catch (Exception e) {
            log.error("创建交易订单失败: {}", e.getMessage(), e);
            return R.fail("创建交易订单失败: " + e.getMessage());
        }
    }

}