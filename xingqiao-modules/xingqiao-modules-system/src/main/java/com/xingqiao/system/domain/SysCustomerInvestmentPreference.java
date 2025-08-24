package com.xingqiao.system.domain;

import com.xingqiao.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 客户投资偏好对象 sys_customer_investment_preference
 *
 * @author xingqiao
 * @date 2025-08-24
 */
public class SysCustomerInvestmentPreference extends BaseEntity
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

    /** 产品类型 */
    @Excel(name = "产品类型")
    private String productType;

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

    public void setProductType(String productType)
    {
        this.productType = productType;
    }

    public String getProductType()
    {
        return productType;
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
                .append("productType", getProductType())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
