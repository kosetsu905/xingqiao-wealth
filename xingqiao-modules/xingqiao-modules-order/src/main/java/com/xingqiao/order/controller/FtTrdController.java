package com.xingqiao.order.controller;

import com.futu.openapi.FTAPI;

import com.futu.openapi.FTAPI_Conn_Trd;
import com.xingqiao.order.service.FtTrdService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ftapi/trade")
class FtTrdController {

    @Autowired
    private FtTrdService tradeService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello from TradeController!";
    }

    /**
     * 获取账户列表
     * 示例：GET /ftapi/trade/get-acc-list?userId=233801605
     */
    @GetMapping("/get-acc-list")
    public CompletableFuture<String> getAccountList(
            @RequestParam(defaultValue = "233801605") long userId) {

        log.info("📥 用户请求获取账户列表 | userID={}", userId);
        return tradeService.getAccountList(userId);
    }


    /**
     * 获取资金信息
     * 示例：GET /ftapi/trade/funds?accId=16097643&env=0&currency=1
     */
    @GetMapping("/funds")
    public CompletableFuture<String> getFunds(
            @RequestParam long accId,
            @RequestParam(defaultValue = "0") int env,
            @RequestParam(defaultValue = "1") int currency) {

        log.info("📥 用户请求获取资金 | accId={} | env={} | currency={}", accId, env, currency);
        return tradeService.getFunds(accId, env, currency);
    }


    /**
     * 获取最大可买卖数量
     * 示例：GET /ftapi/trade/max-qty?accId=16097643&code=00700&price=520&trdEnv=0&orderType=2&trdMarket=1&secMarket=1
     */
    @GetMapping("/max-qty")
    public CompletableFuture<String> getMaxTrdQtys(
            @RequestParam long accId,
            @RequestParam String code,
            @RequestParam double price,
            @RequestParam(defaultValue = "0") int trdEnv,
            @RequestParam(defaultValue = "1") int orderType,
            @RequestParam(defaultValue = "1") int trdMarket,
            @RequestParam(defaultValue = "1") int secMarket) {

        return tradeService.getMaxTrdQtys(
                accId,
                trdEnv,
                trdMarket,
                orderType,
                code,
                price,
                secMarket
        );
    }

    /**
     * 查询指定账户的持仓列表
     * 示例：GET /ftapi/trade/position-list?accId=16097643&trdEnv=0&trdMarket=1
     */
    @GetMapping("/position-list")
    public CompletableFuture<String> getPositionList(
            @RequestParam long accId,
            @RequestParam(defaultValue = "0") int trdEnv,
            @RequestParam(defaultValue = "1") int trdMarket) {

        return tradeService.getPositionList(accId, trdEnv, trdMarket);
    }

    /**
     * 获取单个股票的融资融券比率
     * 示例：GET /ftapi/trade/margin-ratio?accId=16097643&code=00700&secMarket=1&trdEnv=0&trdMarket=1
     */
    @GetMapping("/margin-ratio")
    public CompletableFuture<String> getMarginRatio(
            @RequestParam long accId,
            @RequestParam String code,
            @RequestParam int secMarket,  // 传入股票所在市场，如 1=港股
            @RequestParam(defaultValue = "1") int trdEnv,
            @RequestParam(defaultValue = "1") int trdMarket) {

        return tradeService.getMarginRatio(accId, trdEnv, trdMarket, code, secMarket);
    }

    /**
     * 获取交易流水摘要
     * 示例：GET /ftapi/trade/flow-summary?accId=281756480907782776&trdEnv=1&trdMarket=1&clearingDate=2025-08-18
     */
    @GetMapping("/flow-summary")
    public CompletableFuture<String> getFlowSummary(
            @RequestParam(defaultValue = "16097643") long accId,
            @RequestParam(defaultValue = "0") int trdEnv,  // 默认模拟盘
            @RequestParam(defaultValue = "1") int trdMarket,
            @RequestParam(defaultValue = "2025-02-18") String clearingDate) {

        return tradeService.getFlowSummary(accId, trdEnv, trdMarket, clearingDate);
    }

    /**
     * 下单（买入/卖出）
     * 示例：GET /ftapi/trade/place-order?accId=16097643&trdEnv=0&trdMarket=1&trdSide=1&orderType=2&secMarket=1&code=00700&price=580&qty=100
     */
    @PostMapping("/place-order")
    public CompletableFuture<String> placeOrder(
            @RequestParam long accId,
            @RequestParam(defaultValue = "0") int trdEnv,
            @RequestParam(defaultValue = "1") int trdMarket,
            @RequestParam int trdSide,
            @RequestParam(defaultValue = "2") int orderType,
            @RequestParam(defaultValue = "1") int secMarket,
            @RequestParam String code,
            @RequestParam double price,
            @RequestParam long qty) {

        return tradeService.placeOrder(accId, trdEnv, trdMarket, trdSide, orderType, secMarket, code, price, qty);
    }

    /**
     * 修改订单（包括撤单、改价、改量）
     * 示例：GET /ftapi/trade/modify-order?accId=16097643&trdEnv=0&trdMarket=1&modifyOrderOp=1&orderId=5237901644767803804
     */
    @GetMapping("/modify-order")
    public CompletableFuture<String> modifyOrder(
            @RequestParam long accId,
            @RequestParam(defaultValue = "0") int trdEnv,
            @RequestParam(defaultValue = "1") int trdMarket,
            @RequestParam int modifyOrderOp,
            @RequestParam long orderId,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Long qty) {

        return tradeService.modifyOrder(accId, trdEnv, trdMarket, modifyOrderOp, orderId, price, qty);
    }

    /**
     * 查询当前订单列表
     * 示例：GET /ftapi/trade/order-list?accId=16097643&trdEnv=0&trdMarket=1
     */
    @GetMapping("/order-list")
    public CompletableFuture<String> getOrderList(
            @RequestParam long accId,
            @RequestParam(defaultValue = "0") int trdEnv,
            @RequestParam(defaultValue = "1") int trdMarket) {

        return tradeService.getOrderList(accId, trdEnv, trdMarket);
    }


    /**
     * 查询历史订单列表
     * 示例：GET /ftapi/trade/history-order-list?accId=16097643&beginTime=2025-02-18+00%3A00%3A00&endTime=2025-09-18+23%3A59%3A59
     */
    @GetMapping("/history-order-list")
    public CompletableFuture<String> getHistoryOrderList(
            @RequestParam long accId,
            @RequestParam(defaultValue = "0") int trdEnv,
            @RequestParam(defaultValue = "1") int trdMarket,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        return tradeService.getHistoryOrderList(accId, trdEnv, trdMarket, beginTime, endTime);
    }


    //todo:update order
    @PostMapping("/sub-acc-push")
    public void SubAccPush(long accId)
    {
        tradeService.subscribeAccountPush();
    }

    /**
     * 查询当日成交
     * 示例：GET /ftapi/trade/order-fills?accId=16097643&trdEnv=0&market=1
     */
    @GetMapping("/order-fills")
    public CompletableFuture<String> getOrderFills(
            @RequestParam long accId,
            @RequestParam(defaultValue = "1") int trdEnv,
            @RequestParam(defaultValue = "1") int market) {
        return tradeService.getOrderFillList(accId, trdEnv, market);
    }


    /**
     * 查询历史成交
     * 示例：GET /ftapi/trade/order-history?accId=281756480907782776&trdEnv=0&market=1&beginTime=2025-09-01%2000:00:00&endTime=2025-09-13%2023:59:59
     */
    @GetMapping("/order-history")
    public CompletableFuture<String> getHistoryOrderFills(
            @RequestParam long accId,
            @RequestParam(defaultValue = "0") int trdEnv,        // 默认模拟环境
            @RequestParam(defaultValue = "1") int market,        // 默认港股
            @RequestParam String beginTime,
            @RequestParam String endTime) {

        // 可选：添加基础校验
        if (accId <= 0) {
            throw new IllegalArgumentException("Invalid accId: must be positive");
        }
        if (trdEnv != 0 && trdEnv != 1) {
            throw new IllegalArgumentException("trdEnv must be 0 (paper) or 1 (real)");
        }
        if (market < 1 || market > 3) {
            throw new IllegalArgumentException("market must be 1=HK, 2=US, 3=CN");
        }

        return tradeService.getHistoryOrderFillList(accId, trdEnv, market, beginTime, endTime);
    }
}
