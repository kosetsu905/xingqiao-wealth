package com.xingqiao.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 客户信息对象 sys_customer_info
 *
 * @author xingqiao
 * @date 2025-08-24
 */
public class SysCustomerInfo extends BaseEntity
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

    /** 用户类型（00系统用户,01:合作商用户,02:客户） */
    @Excel(name = "用户类型", readConverterExp = "0=0系统用户,01:合作商用户,02:客户")
    private String userType;

    /** 证件类型（枚举：passport-护照, id_card-身份证, driver_license-驾驶证, other-其他） */
    @Excel(name = "证件类型", readConverterExp = "枚=举：passport-护照,,i=d_card-身份证,,d=river_license-驾驶证,,o=ther-其他")
    private String idType;

    /** 证件号码 */
    @Excel(name = "证件号码")
    private String idNumber;

    /** 证件颁发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "证件颁发日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issueDate;

    /** 证件有效期截止日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "证件有效期截止日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

    /** 头像 URL */
    @Excel(name = "头像 URL")
    private String avatar;

    /** 证件正面照URL */
    @Excel(name = "证件正面照URL")
    private String frontIdFileUrl;

    /** 证件反面照URL */
    @Excel(name = "证件反面照URL")
    private String backIdFileUrl;

    /** 身份是否确认（0-未确认，1-已确认） */
    @Excel(name = "身份是否确认", readConverterExp = "0=-未确认，1-已确认")
    private Integer identityConfirmed;

    /** 全名（真实姓名） */
    @Excel(name = "全名", readConverterExp = "真=实姓名")
    private String fullName;

    /** 年龄（字符串，兼容非数字输入） */
    @Excel(name = "年龄", readConverterExp = "字=符串，兼容非数字输入")
    private String age;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phoneNumber;

    /** 电子邮箱 */
    @Excel(name = "电子邮箱")
    private String email;

    /** 上级经理ID（或账号） */
    @Excel(name = "上级经理ID", readConverterExp = "或=账号")
    private String manager;

    /** 联系地址 */
    @Excel(name = "联系地址")
    private String address;

    /** 个人头像URL */
    @Excel(name = "个人头像URL")
    private String avatarUrl;

    /** 手机区号 */
    @Excel(name = "手机区号")
    private String countryCode;

    /** 性别（0-未知, 1-男, 2-女） */
    @Excel(name = "性别", readConverterExp = "0=-未知,,1=-男,,2=-女")
    private String gender;

    /** 婚姻状况：未婚：SINGLE, 已婚：MARRIED, 离异:DIVORCED, 丧偶:WIDOWED, 其他：OTHER */
    @Excel(name = "婚姻状况：未婚：SINGLE, 已婚：MARRIED, 离异:DIVORCED, 丧偶:WIDOWED, 其他：OTHER")
    private String maritalStatus;

    /** 出生年月日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生年月日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDay;

    /** 子女数量 */
    @Excel(name = "子女数量")
    private Integer childCount;

    /** 部门状态（0正常 1停用） */
    @Excel(name = "部门状态", readConverterExp = "0=正常,1=停用")
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

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setIdType(String idType)
    {
        this.idType = idType;
    }

    public String getIdType()
    {
        return idType;
    }

    public void setIdNumber(String idNumber)
    {
        this.idNumber = idNumber;
    }

    public String getIdNumber()
    {
        return idNumber;
    }

    public void setIssueDate(Date issueDate)
    {
        this.issueDate = issueDate;
    }

    public Date getIssueDate()
    {
        return issueDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setFrontIdFileUrl(String frontIdFileUrl)
    {
        this.frontIdFileUrl = frontIdFileUrl;
    }

    public String getFrontIdFileUrl()
    {
        return frontIdFileUrl;
    }

    public void setBackIdFileUrl(String backIdFileUrl)
    {
        this.backIdFileUrl = backIdFileUrl;
    }

    public String getBackIdFileUrl()
    {
        return backIdFileUrl;
    }

    public void setIdentityConfirmed(Integer identityConfirmed)
    {
        this.identityConfirmed = identityConfirmed;
    }

    public Integer getIdentityConfirmed()
    {
        return identityConfirmed;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public String getAge()
    {
        return age;
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

    public void setManager(String manager)
    {
        this.manager = manager;
    }

    public String getManager()
    {
        return manager;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }

    public void setMaritalStatus(String maritalStatus)
    {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus()
    {
        return maritalStatus;
    }

    public void setBirthDay(Date birthDay)
    {
        this.birthDay = birthDay;
    }

    public Date getBirthDay()
    {
        return birthDay;
    }

    public void setChildCount(Integer childCount)
    {
        this.childCount = childCount;
    }

    public Integer getChildCount()
    {
        return childCount;
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
                .append("userType", getUserType())
                .append("idType", getIdType())
                .append("idNumber", getIdNumber())
                .append("issueDate", getIssueDate())
                .append("expiryDate", getExpiryDate())
                .append("avatar", getAvatar())
                .append("frontIdFileUrl", getFrontIdFileUrl())
                .append("backIdFileUrl", getBackIdFileUrl())
                .append("identityConfirmed", getIdentityConfirmed())
                .append("fullName", getFullName())
                .append("age", getAge())
                .append("phoneNumber", getPhoneNumber())
                .append("email", getEmail())
                .append("manager", getManager())
                .append("address", getAddress())
                .append("avatarUrl", getAvatarUrl())
                .append("countryCode", getCountryCode())
                .append("gender", getGender())
                .append("maritalStatus", getMaritalStatus())
                .append("birthDay", getBirthDay())
                .append("childCount", getChildCount())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
