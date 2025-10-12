package com.xingqiao.api.trade.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 股票区间行情数据
 */
@Data
public class StockChartQuote implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 产品类别 stock | forex | indices | crypto | future | fund
     *
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 交易市场代码
     * 市场代码 （HK、SZ、SH、US、SG、JP、TW、IN、TH、DE、MX、MY、TR、ES、NL、GB、ID、VN），
     */
    private String marketCode;

    /**
     * 交易市场名称
     */
    private String marketName;


    /**
     * 股票代码
     */
    private String stockCode;

    /**
     * 股票名称
     */
    private String stockName;

    private BigDecimal[] openPrices;
    private BigDecimal[] prices;
    private BigDecimal[] pricesHigh;
    private BigDecimal[] pricesLow;
    private BigDecimal[] volumes;
    private String[] timeStamps;
    private BigDecimal priceHigh;
    private BigDecimal priceLow;
    /**
     *  msn数据源图表更新时间
     */
    private LocalDateTime msnDataChartTime;
}
