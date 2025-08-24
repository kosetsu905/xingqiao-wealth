package com.xingqiao.system.domain;

import java.math.BigDecimal;

import com.xingqiao.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 客户财务信息对象 sys_customer_financial
 *
 * @author xingqiao
 * @date 2025-08-24
 */
public class SysCustomerFinancial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long userId;

    /** 客户临时ID */
    @Excel(name = "客户临时ID")
    private String userTempId;

    /** 家庭总资产(万元) */
    @Excel(name = "家庭总资产(万元)")
    private BigDecimal familyTotalAsset;

    /** 家庭负债(万元) */
    @Excel(name = "家庭负债(万元)")
    private BigDecimal familyDebt;

    /** 家庭年收入(万元) */
    @Excel(name = "家庭年收入(万元)")
    private BigDecimal familyAnnualIncome;

    /** 新投资额度(万元) */
    @Excel(name = "新投资额度(万元)")
    private BigDecimal newInvestmentAmount;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserTempId(String userTempId)
    {
        this.userTempId = userTempId;
    }

    public String getUserTempId()
    {
        return userTempId;
    }

    public void setFamilyTotalAsset(BigDecimal familyTotalAsset)
    {
        this.familyTotalAsset = familyTotalAsset;
    }

    public BigDecimal getFamilyTotalAsset()
    {
        return familyTotalAsset;
    }

    public void setFamilyDebt(BigDecimal familyDebt)
    {
        this.familyDebt = familyDebt;
    }

    public BigDecimal getFamilyDebt()
    {
        return familyDebt;
    }

    public void setFamilyAnnualIncome(BigDecimal familyAnnualIncome)
    {
        this.familyAnnualIncome = familyAnnualIncome;
    }

    public BigDecimal getFamilyAnnualIncome()
    {
        return familyAnnualIncome;
    }

    public void setNewInvestmentAmount(BigDecimal newInvestmentAmount)
    {
        this.newInvestmentAmount = newInvestmentAmount;
    }

    public BigDecimal getNewInvestmentAmount()
    {
        return newInvestmentAmount;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("userTempId", getUserTempId())
                .append("familyTotalAsset", getFamilyTotalAsset())
                .append("familyDebt", getFamilyDebt())
                .append("familyAnnualIncome", getFamilyAnnualIncome())
                .append("newInvestmentAmount", getNewInvestmentAmount())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
