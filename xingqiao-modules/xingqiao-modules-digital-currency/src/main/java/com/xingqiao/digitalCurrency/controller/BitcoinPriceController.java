package com.xingqiao.digitalCurrency.controller;

import com.xingqiao.digitalCurrency.service.CoinMarketCapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bitcoin")
public class BitcoinPriceController {
    private final CoinMarketCapService coinMarketCapService;

    public BitcoinPriceController(CoinMarketCapService coinMarketCapService) {
        this.coinMarketCapService = coinMarketCapService;
    }

    @GetMapping("/price")
    public String getBitcoinPrice() {
        double price = coinMarketCapService.getBitcoinPrice();
        return String.format("比特币实时价格（USD）: %.2f", price);
    }
}