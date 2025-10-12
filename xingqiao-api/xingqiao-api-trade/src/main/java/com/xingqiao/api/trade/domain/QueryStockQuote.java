package com.xingqiao.api.trade.domain;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class QueryStockQuote {
    private String productCode;
    private String marketCode;
    private String stockCode;
    /**
     * 图表时间周期
     * 1D1M 一天
     * 5D 5天
     *
     */
    private String type;
}
