package com.xingqiao.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 员工资质认证对象 sys_employee_qualifications
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public class SysEmployeeQualifications extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户类型（00系统用户,01:合作商用户,02:客户） */
    @Excel(name = "用户类型", readConverterExp = "0=0系统用户,01:合作商用户,02:客户")
    private String userType;

    /** 员工信息表表ID */
    @Excel(name = "员工信息表表ID")
    private Long employeeInfoId;

    /** 资质类型（枚举：cfp-金融理财师(CFP), cfa-特许金融分析师(CFA), cpa-注册会计师(CPA), other-其他资质） */
    @Excel(name = "资质类型", readConverterExp = "枚=举：cfp-金融理财师(CFP),,c=fa-特许金融分析师(CFA),,c=pa-注册会计师(CPA),,o=ther-其他资质")
    private String qualificationType;

    /** 资质证书编号 */
    @Excel(name = "资质证书编号")
    private String certificateNumber;

    /** 颁发机构 */
    @Excel(name = "颁发机构")
    private String issuingAuthority;

    /** 证书颁发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "证书颁发日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date certificateIssueDate;

    /** 证书有效期截止日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "证书有效期截止日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date certificateExpiryDate;

    /** 执业年限（字符串，兼容非数字输入） */
    @Excel(name = "执业年限", readConverterExp = "字=符串，兼容非数字输入")
    private String yearsOfPractice;

    /** 资质是否确认（0-未确认，1-已确认） */
    @Excel(name = "资质是否确认", readConverterExp = "0=-未确认，1-已确认")
    private Integer professionalConfirmed;

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

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setEmployeeInfoId(Long employeeInfoId)
    {
        this.employeeInfoId = employeeInfoId;
    }

    public Long getEmployeeInfoId()
    {
        return employeeInfoId;
    }

    public void setQualificationType(String qualificationType)
    {
        this.qualificationType = qualificationType;
    }

    public String getQualificationType()
    {
        return qualificationType;
    }

    public void setCertificateNumber(String certificateNumber)
    {
        this.certificateNumber = certificateNumber;
    }

    public String getCertificateNumber()
    {
        return certificateNumber;
    }

    public void setIssuingAuthority(String issuingAuthority)
    {
        this.issuingAuthority = issuingAuthority;
    }

    public String getIssuingAuthority()
    {
        return issuingAuthority;
    }

    public void setCertificateIssueDate(Date certificateIssueDate)
    {
        this.certificateIssueDate = certificateIssueDate;
    }

    public Date getCertificateIssueDate()
    {
        return certificateIssueDate;
    }

    public void setCertificateExpiryDate(Date certificateExpiryDate)
    {
        this.certificateExpiryDate = certificateExpiryDate;
    }

    public Date getCertificateExpiryDate()
    {
        return certificateExpiryDate;
    }

    public void setYearsOfPractice(String yearsOfPractice)
    {
        this.yearsOfPractice = yearsOfPractice;
    }

    public String getYearsOfPractice()
    {
        return yearsOfPractice;
    }

    public void setProfessionalConfirmed(Integer professionalConfirmed)
    {
        this.professionalConfirmed = professionalConfirmed;
    }

    public Integer getProfessionalConfirmed()
    {
        return professionalConfirmed;
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
                .append("userType", getUserType())
                .append("employeeInfoId", getEmployeeInfoId())
                .append("qualificationType", getQualificationType())
                .append("certificateNumber", getCertificateNumber())
                .append("issuingAuthority", getIssuingAuthority())
                .append("certificateIssueDate", getCertificateIssueDate())
                .append("certificateExpiryDate", getCertificateExpiryDate())
                .append("yearsOfPractice", getYearsOfPractice())
                .append("professionalConfirmed", getProfessionalConfirmed())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
