package com.xingqiao.system.api.domain.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.annotation.Excel;
import com.xingqiao.common.core.web.domain.BaseEntity;
import com.xingqiao.system.api.model.AuditInfoResp;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Data
    public static class AuthAgencyEkyc {
        private static final long serialVersionUID = 1L;

        private Long id; // 主键ID

        //代理人id
        private Long userId;


        private String userType;

        /** 审核备注 */
        private String remark;

        private Integer auditStatus;

        private Date auditTime;
        /** 审核人员ID */
        private Long auditorId;
        /** 审核人员姓名 */
        private String auditorName;


    }

    @Data
    public static class CustomerFinancial {
        private BigDecimal familyTotalAsset;
        private BigDecimal familyDebt;
        private BigDecimal familyAnnualIncome;
        private BigDecimal newInvestmentAmount;

    }

    @Data
    @Getter
    public static class CustomerExtInfo{
        private Long id;
        private Long userId;
        private String userTempId;
        private String fullName;
        private String age;
        private String gender;
        private String phoneNumber;
        private String email;
        private String maritalStatus;
        private Integer childCount;
        private String address;
        private String remark;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
    }

    @Data
    public static class CustomerInvestmentPreference {
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
    }

    @Data
    public static class CustomerQueryInnerRequest {

        private List<Long> idList;
    }

    @Data
    public static class CustomerQueryRequest {

        private String phoneNumber;
        private String email;
        private Integer pageNum;
        private Integer pageSize;
        private Long employeeId;
    }

    @Data
    @Getter
    @Setter
    public static class CustomerSaleSaveRequest {

        private Long id;

        private String userTempId;

        private Long employeeId;

        private Long customerId;

        private String fullName;

        private String phoneNumber;

        private String email;

        private String remark;

        private BigDecimal investmentAmount;


        private String investmentTimeIntent;
        //投资偏好
        List<CustomerInvestmentPreference> interestedProducts;

    }

    @Data
    @Getter
    @Setter
    public static class CustomerSaveRequest {
        private Long employeeId;

        private Long customerId;
        // 客户信息
        private CustomerExtInfo customerInfo;
        // 客户财务信息
        private CustomerFinancial customerFinancial;
        //投资偏好
        List<CustomerInvestmentPreference> investmentPreferences;

    }

    @Data
    public static class Qualifications{
        private String qualificationType; // 资质类型（枚举，如 CFP）


        private String certificateNumber; // 资质证书编号


        private String issuingAuthority; // 颁发机构


        private Date certificateIssueDate; // 证书颁发日期


        private Date certificateExpiryDate; // 证书有效期截止日期


        private String yearsOfPractice; // 执业年限（字符串，兼容非数字输入）


        private List<String> certificateFileUrls; // 资质证书照片URL列表


        private List<String> practiceCertificateFileUrls; // 执业证明文件URL列表


        private Integer professionalConfirmed ; // 资质是否确认（默认未确认）


    }
}

