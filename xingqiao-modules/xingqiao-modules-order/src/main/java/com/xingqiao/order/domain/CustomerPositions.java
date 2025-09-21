package com.xingqiao.order.domain;


import java.math.BigDecimal;

import com.xingqiao.common.core.web.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 客户持仓对象 customer_positions
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Setter
@Getter
public class CustomerPositions extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private String userId;

    /** 资金账户ID */
    @Excel(name = "资金账户ID")
    private String accountId;

    /** 证券ID */
    @Excel(name = "证券ID")
    private String securityId;

    /** 持仓类型: 1-普通, 2-融资, 3-融券 */
    @Excel(name = "持仓类型: 1-普通, 2-融资, 3-融券")
    private Long positionType;

    /** 总持仓数量 */
    @Excel(name = "总持仓数量")
    private BigDecimal quantity;

    /** 可用数量 */
    @Excel(name = "可用数量")
    private BigDecimal availableQuantity;

    /** 冻结数量 */
    @Excel(name = "冻结数量")
    private BigDecimal frozenQuantity;

    /** 持仓成本价 */
    @Excel(name = "持仓成本价")
    private BigDecimal avgCostPrice;

    /** 当前市值 */
    @Excel(name = "当前市值")
    private BigDecimal marketValue;

    /** 浮动盈亏 */
    @Excel(name = "浮动盈亏")
    private BigDecimal floatingPl;

    /** 浮动盈亏比例(%) */
    @Excel(name = "浮动盈亏比例(%)")
    private BigDecimal floatingPlRatio;

    /** 版本号(用于乐观锁) */
    @Excel(name = "版本号(用于乐观锁)")
    private Long version;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("accountId", getAccountId())
                .append("securityId", getSecurityId())
                .append("positionType", getPositionType())
                .append("quantity", getQuantity())
                .append("availableQuantity", getAvailableQuantity())
                .append("frozenQuantity", getFrozenQuantity())
                .append("avgCostPrice", getAvgCostPrice())
                .append("marketValue", getMarketValue())
                .append("floatingPl", getFloatingPl())
                .append("floatingPlRatio", getFloatingPlRatio())
                .append("version", getVersion())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
