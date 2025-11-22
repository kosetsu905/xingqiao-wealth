package com.xingqiao.api.trade.factory;

import com.xingqiao.api.trade.RemoteTradeService;
import com.xingqiao.api.trade.domain.QueryStockQuote;
import com.xingqiao.api.trade.domain.StockQuote;
import com.xingqiao.api.trade.domain.TradeRequest;
import com.xingqiao.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件服务降级处理
 * 
 * @author xingqiao
 */
@Component
public class RemoteTradeFallbackFactory implements FallbackFactory<RemoteTradeService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteTradeFallbackFactory.class);

    @Override
    public RemoteTradeService create(Throwable throwable)
    {
        log.error("交易服务调用失败:{}", throwable.getMessage());
        return new RemoteTradeService()
        {
            @Override
            public R createTrade(TradeRequest tradeRequest, String source) {
                return R.fail("交易服务调用失败：创建订单功能不可用");
            }

            @Override
            public R<StockQuote> getStockQuote(QueryStockQuote queryStockQuote, String source) {
                return null;
            }

            @Override
            public R<List<StockQuote>> getStockQuoteList(List<QueryStockQuote> list, String source) {
                return null;
            }

            @Override
            public R<List<StockQuote>> getStockQuoteHistory(QueryStockQuote queryStockQuote, String source) {
                return null;
            }

            @Override
            public R subscribeStockQuote(QueryStockQuote queryStockQuote, String source) {
                return null;
            }

            @Override
            public R unsubscribeStockQuote(QueryStockQuote queryStockQuote, String source) {
                return null;
            }
        };
    }
}
