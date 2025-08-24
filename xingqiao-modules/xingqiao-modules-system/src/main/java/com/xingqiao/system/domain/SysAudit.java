package com.xingqiao.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xingqiao.common.core.annotation.Excel;

/**
 * 系统审核记录对象 sys_audit
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public class SysAudit extends BaseEntity
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

    /** 业务主表ID */
    @Excel(name = "业务主表ID")
    private Long businessId;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String businessType;

    /** 审核人员ID */
    @Excel(name = "审核人员ID")
    private Long auditorId;

    /** 审核人员姓名 */
    @Excel(name = "审核人员姓名")
    private String auditorName;

    /** 审核状态（0-待审核, 1-审核通过, 2-审核拒绝） */
    @Excel(name = "审核状态", readConverterExp = "0=-待审核,,1=-审核通过,,2=-审核拒绝")
    private Integer auditStatus;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

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

    public void setBusinessId(Long businessId)
    {
        this.businessId = businessId;
    }

    public Long getBusinessId()
    {
        return businessId;
    }

    public void setBusinessType(String businessType)
    {
        this.businessType = businessType;
    }

    public String getBusinessType()
    {
        return businessType;
    }

    public void setAuditorId(Long auditorId)
    {
        this.auditorId = auditorId;
    }

    public Long getAuditorId()
    {
        return auditorId;
    }

    public void setAuditorName(String auditorName)
    {
        this.auditorName = auditorName;
    }

    public String getAuditorName()
    {
        return auditorName;
    }

    public void setAuditStatus(Integer auditStatus)
    {
        this.auditStatus = auditStatus;
    }

    public Integer getAuditStatus()
    {
        return auditStatus;
    }

    public void setAuditTime(Date auditTime)
    {
        this.auditTime = auditTime;
    }

    public Date getAuditTime()
    {
        return auditTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("userType", getUserType())
                .append("businessId", getBusinessId())
                .append("businessType", getBusinessType())
                .append("auditorId", getAuditorId())
                .append("auditorName", getAuditorName())
                .append("auditStatus", getAuditStatus())
                .append("auditTime", getAuditTime())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
