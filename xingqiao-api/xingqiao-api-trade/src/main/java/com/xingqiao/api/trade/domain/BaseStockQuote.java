package com.xingqiao.api.trade.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 通用股票行情实体类
 * 兼容大部分股票行情数据
 */
@Data
public class BaseStockQuote implements Serializable {
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

    /**
     * 当前价
     */
    private BigDecimal currentPrice;


    /**
     * 涨跌额
     */
    private BigDecimal priceChange;

    /**
     * 涨跌幅(百分比)
     */
    private BigDecimal priceChangePercent;

    /**
     * 成交量(股)
     */
    private Long volume;

    /**
     * 成交额(元)
     */
    private BigDecimal amount;


    /**
     * 状态(0:正常, 1:停牌, 2:未开盘)
     */
    private Integer status;

}