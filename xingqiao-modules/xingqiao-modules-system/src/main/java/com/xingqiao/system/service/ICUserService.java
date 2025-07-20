package com.xingqiao.system.service;

import com.xingqiao.system.api.domain.CUser;
import com.xingqiao.system.api.domain.CommonUser;
import com.xingqiao.system.api.domain.SysUser;

import java.util.List;

/**
 * 客户信息Service接口
 *
 * @author xingqiao
 * @date 2025-07-19
 */
public interface ICUserService
{
    /**
     * 查询客户信息
     *
     * @param userId 客户信息主键
     * @return 客户信息
     */
    public CUser selectCUserByUserId(Long userId);

    /**
     * 查询客户信息列表
     *
     * @param cUser 客户信息
     * @return 客户信息集合
     */
    public List<CUser> selectCUserList(CUser cUser);

    /**
     * 新增客户信息
     *
     * @param cUser 客户信息
     * @return 结果
     */
    public int insertCUser(CUser cUser);

    /**
     * 修改客户信息
     *
     * @param cUser 客户信息
     * @return 结果
     */
    public int updateCUser(CUser cUser);

    /**
     * 批量删除客户信息
     *
     * @param userIds 需要删除的客户信息主键集合
     * @return 结果
     */
    public int deleteCUserByUserIds(Long[] userIds);

    /**
     * 删除客户信息信息
     *
     * @param userId 客户信息主键
     * @return 结果
     */
    public int deleteCUserByUserId(Long userId);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkPhoneUnique(CUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean checkEmailUnique(CUser user);
    /**
     * 校验账号是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */

    boolean checkAccountUnique(CUser user);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    CommonUser selectCUserByAccount(String username,String userType);
}
