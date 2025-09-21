package com.xingqiao.order.domain;


import java.math.BigDecimal;

import com.xingqiao.common.core.web.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 账户资金流水对象 customer_account_fund_flows
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Setter
@Getter
public class CustomerAccountFundFlows extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 关联资金账户ID */
    @Excel(name = "关联资金账户ID")
    private String accountId;

    /** 客户ID */
    @Excel(name = "客户ID")
    private String userId;

    /** 币种 */
    @Excel(name = "币种")
    private String currency;

    /** 变动金额 (正:流入, 负:流出) */
    @Excel(name = "变动金额 (正:流入, 负:流出)")
    private BigDecimal amount;

    /** 变动前余额 */
    @Excel(name = "变动前余额")
    private BigDecimal balanceBefore;

    /** 变动后余额 */
    @Excel(name = "变动后余额")
    private BigDecimal balanceAfter;

    /** 流水类型: 1-入金, 2-出金, 3-买入扣款, 4-卖出收款, 5-手续费, 6-税费, 7-利息, 8-分红, 9-融资融券利息, 10-其他 */
    @Excel(name = "流水类型: 1-入金, 2-出金, 3-买入扣款, 4-卖出收款, 5-手续费, 6-税费, 7-利息, 8-分红, 9-融资融券利息, 10-其他")
    private Long flowType;

    /** 关联业务ID (如 trade_id, dividend_id) */
    @Excel(name = "关联业务ID (如 trade_id, dividend_id)")
    private String relatedId;

    /** 关联业务类型: 1-fund_transfers, 2-trades, 3-dividends */
    @Excel(name = "关联业务类型: 1-fund_transfers, 2-trades, 3-dividends")
    private Long relatedType;

    /** 状态: 1-成功, 2-处理中, 3-失败 */
    @Excel(name = "状态: 1-成功, 2-处理中, 3-失败")
    private Long status;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("accountId", getAccountId())
                .append("userId", getUserId())
                .append("currency", getCurrency())
                .append("amount", getAmount())
                .append("balanceBefore", getBalanceBefore())
                .append("balanceAfter", getBalanceAfter())
                .append("flowType", getFlowType())
                .append("relatedId", getRelatedId())
                .append("relatedType", getRelatedType())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
