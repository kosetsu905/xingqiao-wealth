package com.xingqiao.order.config;

import com.xingqiao.api.trade.domain.QueryStockQuote;

public class TradeConstants {

    // 首页全球指数订阅信息
    public static final String STOCK_GLOBAL_INDICES_INFO_PREFIX = "stock:global_indices:info:";
    // 过期时间（30分钟）
    public static final long EXPIRE_TIME = 1800L;
    // 用户websocket登录信息
    public static final String USER_SESSION_KEY_PREFIX = "websocket:user:session:";
    public static final String SESSION_USER_KEY_PREFIX = "websocket:session:user:";
    //会话ID关联用户ID
    public static final String SESSION_KEY_PREFIX = "websocket:session";

    public static String getRedisKey(QueryStockQuote queryStockQuote) {
        return String.format("data:stock:%s:%s:%s",
                queryStockQuote.getProductCode(),
                queryStockQuote.getMarketCode(),
                queryStockQuote.getStockCode());
    }

}
