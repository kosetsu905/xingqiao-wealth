package com.xingqiao.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.CompletableFuture;

import com.xingqiao.order.service.FtQotService;

@Slf4j
@RestController
@RequestMapping("/ftapi/quote")
public class FtQotController {

    @Autowired
    private FtQotService ftQotService;

    /**
     * 获取股票基础行情
     * @param code 股票代码
     * @param market 市场代码（1-港股，2-美股，3-A股等）
     * @return 基础行情JSON数据
     */
    @GetMapping(value = "/GetBasicQot")
    public CompletableFuture<String> getBasicQot(
            @RequestParam("code") String code,
            @RequestParam(value = "market", defaultValue = "1") Integer market) {
        log.info("📤 请求获取基础行情 | code={} | market={}", code, market);
        return ftQotService.getBasicQot(code, market);
    }

    /**
     * 订阅股票行情
     * @param code 股票代码
     * @param market 市场代码（1-港股，2-美股，3-A股等）
     * @return 订阅结果JSON数据
     */
    @GetMapping(value = "/subscribeQot")
    public CompletableFuture<String> subscribeQot(
            @RequestParam("code") String code,
            @RequestParam(value = "market", defaultValue = "1") Integer market) {
        log.info("📤 请求订阅行情 | code={} | market={}", code, market);
        return ftQotService.subscribeQot(code, market);
    }
}