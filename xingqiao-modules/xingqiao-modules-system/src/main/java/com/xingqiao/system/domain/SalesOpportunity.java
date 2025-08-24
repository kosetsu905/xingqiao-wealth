package com.xingqiao.system.domain;

import java.math.BigDecimal;

import com.xingqiao.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 客户意向记录对象 sales_opportunity
 *
 * @author xingqiao
 * @date 2025-08-24
 */
public class SalesOpportunity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long userId;

    /** 客户临时ID */
    @Excel(name = "客户临时ID")
    private String userTempId;

    /** 业务员ID */
    @Excel(name = "业务员ID")
    private Long employeeId;

    /** 客户姓名（必填） */
    @Excel(name = "客户姓名", readConverterExp = "必=填")
    private String fullName;

    /** 联系手机号 */
    @Excel(name = "联系手机号")
    private String phoneNumber;

    /** 联系邮箱 */
    @Excel(name = "联系邮箱")
    private String email;

    /** 投资金额（必填，单位：元） */
    @Excel(name = "投资金额", readConverterExp = "必=填，单位：元")
    private BigDecimal investmentAmount;

    /** 投资时间意向（如：1个月内、半年后等）（必填） */
    @Excel(name = "投资时间意向", readConverterExp = "如=：1个月内、半年后等")
    private String investmentTimeIntent;

    /** 感兴趣的金融产品，如：基金投资,股票投资 或 fund,stock */
    @Excel(name = "感兴趣的金融产品，如：基金投资,股票投资 或 fund,stock")
    private String interestedProducts;

    /** 状态：0-待跟进，1-已跟进，2-已成交，3-已关闭 */
    @Excel(name = "状态：0-待跟进，1-已跟进，2-已成交，3-已关闭")
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

    public void setEmployeeId(Long employeeId)
    {
        this.employeeId = employeeId;
    }

    public Long getEmployeeId()
    {
        return employeeId;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setInvestmentAmount(BigDecimal investmentAmount)
    {
        this.investmentAmount = investmentAmount;
    }

    public BigDecimal getInvestmentAmount()
    {
        return investmentAmount;
    }

    public void setInvestmentTimeIntent(String investmentTimeIntent)
    {
        this.investmentTimeIntent = investmentTimeIntent;
    }

    public String getInvestmentTimeIntent()
    {
        return investmentTimeIntent;
    }

    public void setInterestedProducts(String interestedProducts)
    {
        this.interestedProducts = interestedProducts;
    }

    public String getInterestedProducts()
    {
        return interestedProducts;
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
                .append("employeeId", getEmployeeId())
                .append("fullName", getFullName())
                .append("phoneNumber", getPhoneNumber())
                .append("email", getEmail())
                .append("investmentAmount", getInvestmentAmount())
                .append("investmentTimeIntent", getInvestmentTimeIntent())
                .append("interestedProducts", getInterestedProducts())
                .append("remark", getRemark())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
