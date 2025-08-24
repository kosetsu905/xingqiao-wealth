package com.xingqiao.system.domain;

import com.xingqiao.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 系统地址簿对象 sys_address
 *
 * @author xingqiao
 * @date 2025-08-24
 */
public class SysAddress extends BaseEntity
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

    /** 收件人姓名 */
    @Excel(name = "收件人姓名")
    private String recipientName;

    /** 收件人电话 */
    @Excel(name = "收件人电话")
    private String recipientPhone;

    /** 国家 */
    @Excel(name = "国家")
    private String country;

    /** 省 */
    @Excel(name = "省")
    private String province;

    /** 市 */
    @Excel(name = "市")
    private String city;

    /** 区 */
    @Excel(name = "区")
    private String district;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String detailAddress;

    /** 地址类型：0：住址,:1：公司, 2：收件地址,:3：身份证地址 */
    @Excel(name = "地址类型：0：住址,:1：公司, 2：收件地址,:3：身份证地址")
    private Integer addressType;

    /** 是否默认地址：0-否，1-是 */
    @Excel(name = "是否默认地址：0-否，1-是")
    private Integer isDefault;

    /** 邮政编码 */
    @Excel(name = "邮政编码")
    private String zipCode;

    /** 地址别名，如“公司”、“家里” */
    @Excel(name = "地址别名，如“公司”、“家里”")
    private String addressLabel;

    /** 是否删除：0-否，1-是 */
    @Excel(name = "是否删除：0-否，1-是")
    private Integer isDeleted;

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

    public void setRecipientName(String recipientName)
    {
        this.recipientName = recipientName;
    }

    public String getRecipientName()
    {
        return recipientName;
    }

    public void setRecipientPhone(String recipientPhone)
    {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientPhone()
    {
        return recipientPhone;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCountry()
    {
        return country;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getProvince()
    {
        return province;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public String getDistrict()
    {
        return district;
    }

    public void setDetailAddress(String detailAddress)
    {
        this.detailAddress = detailAddress;
    }

    public String getDetailAddress()
    {
        return detailAddress;
    }

    public void setAddressType(Integer addressType)
    {
        this.addressType = addressType;
    }

    public Integer getAddressType()
    {
        return addressType;
    }

    public void setIsDefault(Integer isDefault)
    {
        this.isDefault = isDefault;
    }

    public Integer getIsDefault()
    {
        return isDefault;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setAddressLabel(String addressLabel)
    {
        this.addressLabel = addressLabel;
    }

    public String getAddressLabel()
    {
        return addressLabel;
    }

    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted()
    {
        return isDeleted;
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
                .append("recipientName", getRecipientName())
                .append("recipientPhone", getRecipientPhone())
                .append("country", getCountry())
                .append("province", getProvince())
                .append("city", getCity())
                .append("district", getDistrict())
                .append("detailAddress", getDetailAddress())
                .append("addressType", getAddressType())
                .append("isDefault", getIsDefault())
                .append("zipCode", getZipCode())
                .append("addressLabel", getAddressLabel())
                .append("isDeleted", getIsDeleted())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
