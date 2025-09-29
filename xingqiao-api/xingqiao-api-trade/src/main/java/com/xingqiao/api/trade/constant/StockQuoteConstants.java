package com.xingqiao.api.trade.constant;

/**
 * 股票行情相关常量
 */
public interface StockQuoteConstants {
    /**
     * 数据类型常量
     */
    String DATA_TYPE_DAILY = "D"; // 日K
    String DATA_TYPE_WEEKLY = "W"; // 周K
    String DATA_TYPE_MONTHLY = "M"; // 月K
    String DATA_TYPE_MINUTE_1 = "1"; // 1分钟线
    String DATA_TYPE_MINUTE_5 = "5"; // 5分钟线
    String DATA_TYPE_MINUTE_15 = "15"; // 15分钟线
    String DATA_TYPE_MINUTE_30 = "30"; // 30分钟线
    String DATA_TYPE_MINUTE_60 = "60"; // 60分钟线

    /**
     * 市场代码常量
     */
    String MARKET_CODE_SH = "SH"; // 上海证券交易所
    String MARKET_CODE_SZ = "SZ"; // 深圳证券交易所
    String MARKET_CODE_HK = "HK"; // 香港证券交易所
    String MARKET_CODE_US = "US"; // 美国证券交易所
    String MARKET_CODE_OTHER = "OTHER"; // 其他市场

    /**
     * 市场名称常量
     */
    String MARKET_NAME_SH = "上海证券交易所"; // 上海证券交易所
    String MARKET_NAME_SZ = "深圳证券交易所"; // 深圳证券交易所
    String MARKET_NAME_HK = "香港证券交易所"; // 香港证券交易所
    String MARKET_NAME_US = "美国证券交易所"; // 美国证券交易所

    /**
     * 股票状态常量
     */
    Integer STATUS_NORMAL = 0; // 正常
    Integer STATUS_SUSPENDED = 1; // 停牌
    Integer STATUS_NOT_OPEN = 2; // 未开盘

    /**
     * 数据源常量
     */
    String DATA_SOURCE_SINA = "SINA"; // 新浪财经
    String DATA_SOURCE_BAIDU = "BAIDU"; // 百度财经
    String DATA_SOURCE_EASTMONEY = "EASTMONEY"; // 东方财富
    String DATA_SOURCE_TENCENT = "TENCENT"; // 腾讯财经
    String DATA_SOURCE_YAHOO = "YAHOO"; // 雅虎财经

    /**
     * API路径相关常量
     */
    String API_PREFIX_QUOTE = "/api/quote"; // 行情API前缀
    String API_PATH_GET_QUOTE = "/get"; // 获取行情
    String API_PATH_GET_QUOTE_LIST = "/list"; // 获取行情列表
    String API_PATH_GET_HISTORY = "/history"; // 获取历史行情
    String API_PATH_SUBSCRIBE = "/subscribe"; // 订阅行情

    /**
     * 时间格式常量
     */
    String TIME_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss"; // ISO标准时间格式
    String TIME_FORMAT_DATE = "yyyy-MM-dd"; // 日期格式
    String TIME_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"; // 日期时间格式

    /**
     * 请求参数常量
     */
    String PARAM_STOCK_CODE = "stockCode"; // 股票代码
    String PARAM_MARKET_CODE = "marketCode"; // 市场代码
    String PARAM_DATA_TYPE = "dataType"; // 数据类型
    String PARAM_START_DATE = "startDate"; // 开始日期
    String PARAM_END_DATE = "endDate"; // 结束日期
    String PARAM_LIMIT = "limit"; // 限制条数
    String PARAM_OFFSET = "offset"; // 偏移量
}