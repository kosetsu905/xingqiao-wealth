package com.xingqiao.system.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.system.api.domain.Qualifications;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SysEmployeeInfoResp {


    /** 用户ID */
    private Long userId;

    /** 头像 */
    private String avatar;

    /** 姓名 */
    private String fullName;


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
    /** 资质证书 */
    private List<Qualifications> qualifications;


}
