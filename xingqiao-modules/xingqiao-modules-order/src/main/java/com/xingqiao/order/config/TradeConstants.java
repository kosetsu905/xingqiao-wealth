package com.xingqiao.order.config;

public class TradeConstants {

    // 用于存储股票的详细订阅信息
    public static final String STOCK_INFO_PREFIX = "stock:info:";
    // 过期时间（30分钟）
    public static final long EXPIRE_TIME = 1800L;
    // 用户websocket登录信息
    public static final String USER_SESSION_KEY_PREFIX = "websocket:user:session:";
    public static final String SESSION_USER_KEY_PREFIX = "websocket:session:user:";
    //会话ID关联用户ID
    public static final String SESSION_KEY_PREFIX = "websocket:session";



}
