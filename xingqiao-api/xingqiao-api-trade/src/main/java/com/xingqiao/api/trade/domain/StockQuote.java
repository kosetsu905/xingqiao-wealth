package com.xingqiao.api.trade.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 通用股票行情实体类
 * 兼容大部分股票行情数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StockQuote extends BaseStockQuote  {
    private static final long serialVersionUID = 1L;

    /**
     * 开盘价
     */
    private BigDecimal openPrice;

    /**
     * 最高价
     */
    private BigDecimal highPrice;

    /**
     * 最低价
     */
    private BigDecimal lowPrice;

    /**
     * 收盘价
     */
    private BigDecimal closePrice;

    /**
     * 前收盘价
     */
    private BigDecimal prevClosePrice;


    /**
     * 换手率(百分比)
     */
    private BigDecimal turnoverRate;

    /**
     * 市盈率(TTM)
     */
    private BigDecimal peTTM;

    /**
     * 市净率
     */
    private BigDecimal pb;

    /**
     * 总市值(元)
     */
    private BigDecimal totalMarketCap;

    /**
     * 流通市值(元)
     */
    private BigDecimal circulationMarketCap;

    /**
     * 行情时间
     */
    private LocalDateTime timeLastUpdated;

    /**
     * 数据类型(D - 日K, W - 周K, M - 月K, 1 - 1分钟线等)
     */
    private String dataType;

    /**
     * 涨停价
     */
    private BigDecimal limitUpPrice;

    /**
     * 跌停价
     */
    private BigDecimal limitDownPrice;


    /**
     * 均价
     */
    private BigDecimal avgPrice;

    /**
     * 52周最高价
     */
    private BigDecimal week52High;

    /**
     * 52周最低价
     */
    private BigDecimal week52Low;

    /**
     * 振幅(百分比)
     */
    private BigDecimal amplitude;

    /**
     * 量比
     */
    private BigDecimal volumeRatio;

    /**
     * 委比
     */
    private BigDecimal commissionRatio;

    /**
     * 内盘
     */
    private Long innerMarketVolume;

    /**
     * 外盘
     */
    private Long outerMarketVolume;

    /**
     * 买一价
     */
    private BigDecimal bidPrice1;

    /**
     * 买一量
     */
    private Long bidVolume1;

    /**
     * 卖一价
     */
    private BigDecimal askPrice1;

    /**
     * 卖一量
     */
    private Long askVolume1;

    private String initial;

    private String bgColor;

    private String textColor;

    /**
     * 股票行情图表数据
     */
    private StockChartQuote stockChartQuote;


    /**
     * 数据源
     */
    private String dataSource;


    /**
     *  msn数据源更新时间
     */
    private LocalDateTime msnDataTime;


    /**
     *  itick数据源更新时间
     */
    private LocalDateTime itickDataTime;
}
