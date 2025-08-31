package com.xingqiao.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.web.domain.BaseEntity;
import com.xingqiao.system.api.model.AuditInfoResp;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AgencyEkyc extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id; // 主键ID

    private String idType; // 证件类型（枚举）

    private String idNumber; // 证件号码

    private Date issueDate; // 证件颁发日期

    private Date expiryDate; // 证件有效期截止日期

    private String frontIdFileUrl; // 证件正面照URL

    private String backIdFileUrl; // 证件反面照URL

    private Boolean identityConfirmed ; // 身份是否确认（默认未确认）

    // ------------------------------ 专业资质信息 ------------------------------

    private List<Qualifications> qualifications;

    private String professionalExperience; // 专业经验描述

    // ------------------------------ 个人信息 ------------------------------

    private String accountName; // 账户名称（登录用）


    private String fullName; // 全名（真实姓名）

    //审核状态
    private String status;


    private String age; // 年龄（字符串，兼容非数字输入）


    private String phoneNumber; // 手机号码


    private String countryCode; // 手机区号


    private String gender; // 性别（0-未知, 1-男, 2-女）


    private Date birthday; // 出生年月日


    private String email; // 电子邮箱


    private String manager; // 上级经理ID（或账号）


    private String address; // 联系地址

    
    private String avatarUrl; // 个人头像URL

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Long userId;

    //审核信息
    private List<AuditInfoResp> auditInfoRespList;

}

