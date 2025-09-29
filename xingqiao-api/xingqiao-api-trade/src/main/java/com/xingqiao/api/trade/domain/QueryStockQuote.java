package com.xingqiao.api.trade.domain;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class QueryStockQuote {
    private String productCode;
    private String marketCode;
    private String stockCode;
}
