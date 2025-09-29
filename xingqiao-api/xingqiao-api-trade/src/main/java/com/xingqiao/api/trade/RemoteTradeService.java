package com.xingqiao.api.trade;

import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.api.trade.factory.RemoteTradeFallbackFactory;
import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.common.core.constant.ServiceNameConstants;
import com.xingqiao.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 交易服务接口
 * 提供交易相关的远程调用服务
 * 
 * @author xingqiao
 * @date 2025-09-22
 */
@FeignClient(contextId = "quoteServiceApi", value = ServiceNameConstants.TRADE_SERVICE, fallbackFactory = RemoteTradeFallbackFactory.class)
public interface RemoteTradeService {

    /**
     * 查询交易详情
     * @param tradeId 交易ID
     * @return 交易详情
     */
    @GetMapping("/api/trade/detail")
    R getTradeDetail(@RequestParam("tradeId") Long tradeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 创建交易订单
     * @param tradeRequest 交易请求参数
     * @return 交易结果
     */
    @PostMapping("/api/trade/create")
    R createTrade(@RequestBody TradeRequest tradeRequest, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 取消交易订单
     * @param tradeId 交易ID
     * @return 取消结果
     */
    @PostMapping("/api/trade/cancel")
    R cancelTrade(@RequestParam("tradeId") Long tradeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 获取单只股票行情
     * @return 股票行情信息
     */
    @PostMapping("/api/quote/get")
    R<StockQuote> getStockQuote(@RequestBody QueryStockQuote queryStockQuote, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 批量获取股票行情
     * @return 股票行情列表
     */
    @PostMapping("/api/quote/list")
    R<List<StockQuote>> getStockQuoteList(@RequestBody List<QueryStockQuote> list, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 获取历史行情数据
     * @return 历史行情列表
     */
    @GetMapping("/api/quote/history")
    R<List<StockQuote>> getStockQuoteHistory(@RequestBody  QueryStockQuote queryStockQuote, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 订阅单个股票行情
     * @return 订阅结果
     */
    @PostMapping("/api/quote/subscribe")
    R subscribeStockQuote(@RequestBody  QueryStockQuote queryStockQuote, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 取消订阅单个股票行情
     * @return 取消订阅结果
     */
    @PostMapping("/api/quote/unsubscribe")
    R unsubscribeStockQuote(@RequestBody  QueryStockQuote queryStockQuote, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}