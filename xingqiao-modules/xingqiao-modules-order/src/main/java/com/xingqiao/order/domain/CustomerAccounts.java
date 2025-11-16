package com.xingqiao.order.domain;

import java.math.BigDecimal;

import com.xingqiao.common.core.web.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 客户资金账户对象 customer_accounts
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Setter
@Getter
public class CustomerAccounts extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long userId;

    /** 币种 (如 CNY, USD) */
    @Excel(name = "币种 (如 CNY, USD)")
    private String currency;

    /** 总资金 */
    @Excel(name = "总资金")
    private BigDecimal totalBalance;

    /** 可用资金 */
    @Excel(name = "可用资金")
    private BigDecimal availableBalance;

    /** 冻结资金 */
    @Excel(name = "冻结资金")
    private BigDecimal frozenBalance;

    /** 可取资金 */
    @Excel(name = "可取资金")
    private BigDecimal withdrawableBalance;

    /** 信用资金(融资融券) */
    @Excel(name = "信用资金(融资融券)")
    private BigDecimal creditBalance;

    /** 版本号(用于乐观锁) */
    @Excel(name = "版本号(用于乐观锁)")
    private Long version;

    /** 状态: 1-正常, 2-冻结, 3-销户 */
    @Excel(name = "状态: 1-正常, 2-冻结, 3-销户")
    private Long status;
    
    /** 账户类型: 1-实盘账户, 2-虚拟账户 */
    @Excel(name = "账户类型")
    private Integer accountType;
    
    /** 账户名称 */
    @Excel(name = "账户名称")
    private String accountName;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("currency", getCurrency())
                .append("totalBalance", getTotalBalance())
                .append("availableBalance", getAvailableBalance())
                .append("frozenBalance", getFrozenBalance())
                .append("withdrawableBalance", getWithdrawableBalance())
                .append("creditBalance", getCreditBalance())
                .append("version", getVersion())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("accountType", getAccountType())
                .append("accountName", getAccountName())
                .toString();
    }
}

