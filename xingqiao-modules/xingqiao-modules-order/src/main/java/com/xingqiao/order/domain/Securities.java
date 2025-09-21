package com.xingqiao.order.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.web.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 证券信息对象 securities
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Setter
@Getter
public class Securities extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 证券代码 (如 600519) */
    @Excel(name = "证券代码 (如 600519)")
    private String securityCode;

    /** 证券名称 (如 贵州茅台) */
    @Excel(name = "证券名称 (如 贵州茅台)")
    private String securityName;

    /** 交易所代码 (如 SSE, SZSE) */
    @Excel(name = "交易所代码 (如 SSE, SZSE)")
    private String exchangeCode;

    /** 证券类型: 1-股票, 2-基金, 3-债券, 4-期货, 5-期权,6-数字货币 */
    @Excel(name = "证券类型: 1-股票, 2-基金, 3-债券, 4-期货, 5-期权,6-数字货币")
    private Long securityType;

    /** 交易币种 */
    @Excel(name = "交易币种")
    private String currency;

    /** 交易单位 (每手股数) */
    @Excel(name = "交易单位 (每手股数)")
    private Long lotSize;

    /** 最小变动价位 */
    @Excel(name = "最小变动价位")
    private BigDecimal tickSize;

    /** 涨跌幅限制(%) */
    @Excel(name = "涨跌幅限制(%)")
    private BigDecimal priceLimit;

    /** 是否支持融资融券: 0-否, 1-是 */
    @Excel(name = "是否支持融资融券: 0-否, 1-是")
    private Integer isMargin;

    /** 状态: 1-交易, 2-停牌, 3-退市 */
    @Excel(name = "状态: 1-交易, 2-停牌, 3-退市")
    private Long status;

    /** 上市日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上市日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date listedDate;

    /** 退市日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "退市日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date delistedDate;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("securityCode", getSecurityCode())
                .append("securityName", getSecurityName())
                .append("exchangeCode", getExchangeCode())
                .append("securityType", getSecurityType())
                .append("currency", getCurrency())
                .append("lotSize", getLotSize())
                .append("tickSize", getTickSize())
                .append("priceLimit", getPriceLimit())
                .append("isMargin", getIsMargin())
                .append("status", getStatus())
                .append("listedDate", getListedDate())
                .append("delistedDate", getDelistedDate())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
