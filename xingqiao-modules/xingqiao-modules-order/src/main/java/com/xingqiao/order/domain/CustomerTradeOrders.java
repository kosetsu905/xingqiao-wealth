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
 * 交易订单对象 customer_trade_orders
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Setter
@Getter
public class CustomerTradeOrders extends BaseEntity
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

    /** 订单类型: 1-限价单, 2-市价单, 3-条件单 */
    @Excel(name = "订单类型: 1-限价单, 2-市价单, 3-条件单")
    private Long orderType;

    /** 买卖方向: 1-买入, 2-卖出 */
    @Excel(name = "买卖方向: 1-买入, 2-卖出")
    private Long direction;

    /** 委托价格(市价单可为0) */
    @Excel(name = "委托价格(市价单可为0)")
    private BigDecimal price;

    /** 委托数量 */
    @Excel(name = "委托数量")
    private BigDecimal quantity;

    /** 委托金额(估算) */
    @Excel(name = "委托金额(估算)")
    private BigDecimal amount;

    /** 订单状态: 0-待报, 1-已报, 2-部分成交, 3-完全成交, 4-部分撤单, 5-完全撤单, 6-废单 */
    @Excel(name = "订单状态: 0-待报, 1-已报, 2-部分成交, 3-完全成交, 4-部分撤单, 5-完全撤单, 6-废单")
    private Long status;

    /** 已成交数量 */
    @Excel(name = "已成交数量")
    private BigDecimal filledQuantity;

    /** 已成交金额 */
    @Excel(name = "已成交金额")
    private BigDecimal filledAmount;

    /** 平均成交价格 */
    @Excel(name = "平均成交价格")
    private BigDecimal avgFilledPrice;

    /** 预估手续费 */
    @Excel(name = "预估手续费")
    private BigDecimal estimatedFee;

    /** 预估税费 */
    @Excel(name = "预估税费")
    private BigDecimal estimatedTax;

    /** 实际手续费 */
    @Excel(name = "实际手续费")
    private BigDecimal actualFee;

    /** 实际税费 */
    @Excel(name = "实际税费")
    private BigDecimal actualTax;

    /** 订单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderTime;

    /** 订单过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "订单过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 报单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportTime;

    /** 撤单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "撤单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date cancelTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date finishTime;

    /** 条件单类型: 1-价格条件, 2-时间条件 */
    @Excel(name = "条件单类型: 1-价格条件, 2-时间条件")
    private Long conditionType;

    /** 条件值 */
    @Excel(name = "条件值")
    private BigDecimal conditionValue;

    /** 是否通过风控检查: 0-否, 1-是 */
    @Excel(name = "是否通过风控检查: 0-否, 1-是")
    private Integer riskChecked;

    /** 风控备注 */
    @Excel(name = "风控备注")
    private String riskRemark;

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
                .append("orderType", getOrderType())
                .append("direction", getDirection())
                .append("price", getPrice())
                .append("quantity", getQuantity())
                .append("amount", getAmount())
                .append("status", getStatus())
                .append("filledQuantity", getFilledQuantity())
                .append("filledAmount", getFilledAmount())
                .append("avgFilledPrice", getAvgFilledPrice())
                .append("estimatedFee", getEstimatedFee())
                .append("estimatedTax", getEstimatedTax())
                .append("actualFee", getActualFee())
                .append("actualTax", getActualTax())
                .append("orderTime", getOrderTime())
                .append("expireTime", getExpireTime())
                .append("reportTime", getReportTime())
                .append("cancelTime", getCancelTime())
                .append("finishTime", getFinishTime())
                .append("conditionType", getConditionType())
                .append("conditionValue", getConditionValue())
                .append("riskChecked", getRiskChecked())
                .append("riskRemark", getRiskRemark())
                .append("version", getVersion())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
