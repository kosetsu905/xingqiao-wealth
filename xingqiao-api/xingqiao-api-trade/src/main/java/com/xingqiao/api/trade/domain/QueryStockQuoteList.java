package com.xingqiao.api.trade.domain;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class QueryStockQuoteList {

    private List<QueryStockQuote> list;
    private Long userId;
    private String sessionId;
    private String dataType;
}
