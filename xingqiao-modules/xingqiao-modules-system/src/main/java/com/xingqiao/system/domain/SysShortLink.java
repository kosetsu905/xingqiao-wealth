package com.xingqiao.system.domain;

import com.xingqiao.common.core.annotation.Excel;
import com.xingqiao.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 短链接对象 sys_short_link
 * 
 * @author xingqiao
 */
public class SysShortLink extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long shortLinkId;

    /** 短链接码 */
    @Excel(name = "短链接码")
    private String shortCode;

    /** 原始链接 */
    @Excel(name = "原始链接")
    private String originalUrl;

    /** 点击次数 */
    @Excel(name = "点击次数")
    private Long clickCount;

    /** 是否启用（0启用 1停用） */
    @Excel(name = "是否启用", readConverterExp = "0=启用,1=停用")
    private String status;

    public void setShortLinkId(Long shortLinkId) 
    {
        this.shortLinkId = shortLinkId;
    }

    public Long getShortLinkId() 
    {
        return shortLinkId;
    }
    public void setShortCode(String shortCode) 
    {
        this.shortCode = shortCode;
    }

    public String getShortCode() 
    {
        return shortCode;
    }
    public void setOriginalUrl(String originalUrl) 
    {
        this.originalUrl = originalUrl;
    }

    public String getOriginalUrl() 
    {
        return originalUrl;
    }
    public void setClickCount(Long clickCount) 
    {
        this.clickCount = clickCount;
    }

    public Long getClickCount() 
    {
        return clickCount;
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
            .append("shortLinkId", getShortLinkId())
            .append("shortCode", getShortCode())
            .append("originalUrl", getOriginalUrl())
            .append("clickCount", getClickCount())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}