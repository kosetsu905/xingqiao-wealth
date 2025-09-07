package com.xingqiao.system.api.domain.client;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.annotation.Excel;
import com.xingqiao.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户KYC认证记录对象 customer_kyc_records
 *
 * @author xingqiao
 * @date 2025-09-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class CustomerKycRecordsReq extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户ID */
    @Excel(name = "客户ID")
    private Long userId;

    /** 证件类型（枚举：passport-护照, id_card-身份证, driver_license-驾驶证, other-其他） */
    @Excel(name = "证件类型", readConverterExp = "枚=举：passport-护照,,i=d_card-身份证,,d=river_license-驾驶证,,o=ther-其他")
    private String idType;
    /** 全名（真实姓名） */
    @Excel(name = "全名", readConverterExp = "真=实姓名")
    private String fullName;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phoneNumber;
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

    /** 出生年月日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生年月日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDay;

    /** 证件正面照URL */
    @Excel(name = "证件正面照URL")
    private String frontIdFileUrl;

    /** 证件反面照URL */
    @Excel(name = "证件反面照URL")
    private String backIdFileUrl;

    /** 人脸验证状态:0-未验证,1-验证中,2-验证成功,3-验证失败 */
    @Excel(name = "人脸验证状态:0-未验证,1-验证中,2-验证成功,3-验证失败")
    private Long faceVerifyStatus;

    /** 人脸验证时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "人脸验证时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date faceVerifyTime;
    /** 电子邮箱 */
    @Excel(name = "电子邮箱")
    private String email;

    /** 人脸比对分数 */
    @Excel(name = "人脸比对分数")
    private BigDecimal faceVerifyScore;

    /** 人脸照片URL */
    @Excel(name = "人脸照片URL")
    private String faceImageUrl;

    /** 审核状态:0-待审核,1-审核通过,2-审核拒绝 */
    @Excel(name = "审核状态:0-待审核,1-审核通过,2-审核拒绝")
    private Long reviewStatus;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewTime;

    /** 审核操作员 */
    @Excel(name = "审核操作员")
    private String reviewOperator;

    /** 三方id */
    @Excel(name = "三方id")
    private String certifyId;
    /** 审核意见 */
    @Excel(name = "审核意见")
    private String reviewComment;

    /** 设备信息 */
    @Excel(name = "设备信息")
    private String deviceInfo;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddress;

    /**
     * 步骤
     * */
    private Integer step;



}
