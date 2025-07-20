package com.xingqiao.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xingqiao.common.core.annotation.Excel;
import com.xingqiao.common.core.annotation.Excels;
import com.xingqiao.common.core.constant.UserConstants;
import com.xingqiao.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommonUser extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /**  */
    @Excel(name = "")
    private String userName;

    /**  */
    @Excel(name = "")
    private String nickName;

    /** 用户类型（00系统用户，01客户，02业务员） */
    @Excel(name = "用户类型", readConverterExp = "0=0系统用户，01客户，02业务员")
    private String userType;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phoneNumber;

    /** 用户性别（0男 1女 2未知） */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String avatar;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 账号状态（0正常 1停用） */
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登录IP */
    @Excel(name = "最后登录IP")
    private String loginIp;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginDate;

    /** 密码最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "密码最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pwdUpdateDate;

    /** 手机国家区号 */
    @Excel(name = "手机国家区号")
    private String countryCode;

    /** 登录账号 */
    @Excel(name = "登录账号")
    private String account;

    /** 部门对象 */
    @Excels({
            @Excel(name = "部门名称", targetAttr = "deptName", type = Excel.Type.EXPORT),
            @Excel(name = "部门负责人", targetAttr = "leader", type = Excel.Type.EXPORT)
    })
    private SysDept dept;

    /** 角色对象 */
    private List<SysRole> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;

    /** 角色ID */
    private Long roleId;


    /** 部门ID */
    @Excel(name = "部门编号", type = Excel.Type.IMPORT)
    private Long deptId;


    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return UserConstants.isAdmin(userId);
    }

}
