package com.xingqiao.order.service;

import com.alibaba.fastjson2.JSONObject;
import com.xingqiao.api.trade.domain.QueryStockQuoteList;
import com.xingqiao.api.trade.domain.SubscribeRequest;
import com.xingqiao.common.core.domain.R;

public interface WebSocketSubscribeService {
    R subscribeStockQuote(QueryStockQuoteList queryStockQuoteList);
    R unsubscribeStockQuote(QueryStockQuoteList queryStockQuoteList);

    void subscribe(SubscribeRequest subscribeRequest);

    void unsubscribe(SubscribeRequest subscribeRequest);
}
