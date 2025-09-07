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
 * 客户信息响应
 *
 * @author xingqiao
 * @date 2025-09-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class ClientCustomerResp extends BaseEntity
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

    /** 家庭主要经济来源 */
    @Excel(name = "家庭主要经济来源")
    private String primaryIncomeSource;

    /** 性别（0-未知, 1-男, 2-女） */
    @Excel(name = "性别", readConverterExp = "0=-未知,,1=-男,,2=-女")
    private String gender;

    /** 是否有房贷（0-未知, 1-是, 2-否） */
    @Excel(name = "是否有房贷", readConverterExp = "0=-未知,,1=-是,,2=-否")
    private String hasMortgage;

    /** 是否需要赡养（0-未知, 1-是, 2-否） */
    @Excel(name = "是否需要赡养", readConverterExp = "0=-未知,,1=-是,,2=-否")
    private String requiresSupport;

    /** 婚姻状况：未婚：SINGLE, 已婚：MARRIED, 离异:DIVORCED, 丧偶:WIDOWED, 其他：OTHER */
    @Excel(name = "婚姻状况：未婚：SINGLE, 已婚：MARRIED, 离异:DIVORCED, 丧偶:WIDOWED, 其他：OTHER")
    private String maritalStatus;

    /** 出生年月日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生年月日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDay;

    /** 子女数量 */
    @Excel(name = "子女数量")
    private String childCount;

    /** 三方id */
    @Excel(name = "三方id")
    private String certifyId;

    /** 人脸验证状态:0-未验证,1-验证中,2-验证成功,3-验证失败 */
    @Excel(name = "人脸验证状态:0-未验证,1-验证中,2-验证成功,3-验证失败")
    private Long faceVerifyStatus;

    /** 人脸验证时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "人脸验证时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date faceVerifyTime;

    /** 人脸比对分数 */
    @Excel(name = "人脸比对分数")
    private BigDecimal faceVerifyScore;

    /** 人脸照片URL */
    @Excel(name = "人脸照片URL")
    private String faceImageUrl;

    /** 审核状态:0-待审核,1-审核通过,2-审核拒绝 */
    @Excel(name = "审核状态:0-待审核,1-审核通过,2-审核拒绝")
    private String status;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewTime;

    /** 审核操作员 */
    @Excel(name = "审核操作员")
    private String reviewOperator;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String reviewComment;

    /** 房贷月供 */
    @Excel(name = "房贷月供")
    private BigDecimal monthlyMortgagePayment;

    /** 子女教育支出 */
    @Excel(name = "子女教育支出")
    private BigDecimal childrenEducationExpense;

    /** 家庭人数 */
    @Excel(name = "家庭人数")
    private Long familyCount;

}
