package com.xingqiao.system.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.system.api.domain.employee.AgencyEkyc;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SysEmployeeInfoResp {


    /** 业务id */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 用户类型 */
    private String userType;
    /** 头像 */
    private String avatar;

    /** 姓名 */
    private String fullName;


    /** 证件类型（枚举：passport-护照, id_card-身份证, driver_license-驾驶证, other-其他） */
    private String idType;

    /** 证件号码 */
    private String idNumber;
    /** 证件颁发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;


    /** 证件正面照URL */
    private String frontIdFileUrl;

    /** 证件反面照URL */
    private String backIdFileUrl;


    /** 专业经验描述 */
    private String professionalExperience;

    /** 联系地址 */
    private String address;

    /** 证件有效期截止日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    /** 职位 */
    private String position;
    /** 入职时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date hireDate;
    /** 客户数量 */
    private Integer clientCount;
    /** 管理资产 */
    private BigDecimal managedAssets;
    /** 邮箱 */
    private String email;
    /** 手机号 */
    private String phoneNumber;
    /** 部门名称 */
    private String deptName;
    /** 职称 */
    private String level;


    /** 手机区号 */
    private String countryCode;

    /** 性别（0-未知, 1-男, 2-女） */
    private String gender;

    /** 出生年月日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


    /** 备注 */
    private String remark;
    /** 资质证书 */
    private List<AgencyEkyc.Qualifications> qualifications;

    //审核信息
    private List<AuditInfoResp> auditInfoRespList;

}
