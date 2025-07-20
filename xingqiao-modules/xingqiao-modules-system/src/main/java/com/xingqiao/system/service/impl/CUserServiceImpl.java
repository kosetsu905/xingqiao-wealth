package com.xingqiao.system.service.impl;

import java.util.List;

import com.xingqiao.common.core.constant.UserConstants;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.common.core.utils.StringUtils;
import com.xingqiao.system.api.domain.CUser;
import com.xingqiao.system.api.domain.CommonUser;
import com.xingqiao.system.api.domain.SysUser;
import com.xingqiao.system.mapper.CUserExtMapper;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.CUserMapper;
import com.xingqiao.system.service.ICUserService;

import javax.annotation.Resource;

/**
 * 客户信息Service业务层处理
 *
 * @author xingqiao
 * @date 2025-07-19
 */
@Service
public class CUserServiceImpl implements ICUserService
{
    @Resource
    private CUserMapper cUserMapper;
    @Resource
    private CUserExtMapper cUserExtMapper;

    /**
     * 查询客户信息
     *
     * @param userId 客户信息主键
     * @return 客户信息
     */
    @Override
    public CUser selectCUserByUserId(Long userId)
    {
        return cUserMapper.selectCUserByUserId(userId);
    }

    /**
     * 查询客户信息列表
     *
     * @param cUser 客户信息
     * @return 客户信息
     */
    @Override
    public List<CUser> selectCUserList(CUser cUser)
    {
        return cUserMapper.selectCUserList(cUser);
    }

    /**
     * 新增客户信息
     *
     * @param cUser 客户信息
     * @return 结果
     */
    @Override
    public int insertCUser(CUser cUser)
    {
        cUser.setCreateTime(DateUtils.getNowDate());
        return cUserMapper.insertCUser(cUser);
    }

    /**
     * 修改客户信息
     *
     * @param cUser 客户信息
     * @return 结果
     */
    @Override
    public int updateCUser(CUser cUser)
    {
        cUser.setUpdateTime(DateUtils.getNowDate());
        return cUserMapper.updateCUser(cUser);
    }

    /**
     * 批量删除客户信息
     *
     * @param userIds 需要删除的客户信息主键
     * @return 结果
     */
    @Override
    public int deleteCUserByUserIds(Long[] userIds)
    {
        return cUserMapper.deleteCUserByUserIds(userIds);
    }

    /**
     * 删除客户信息信息
     *
     * @param userId 客户信息主键
     * @return 结果
     */
    @Override
    public int deleteCUserByUserId(Long userId)
    {
        return cUserMapper.deleteCUserByUserId(userId);
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(CUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        CommonUser info = cUserExtMapper.checkPhoneUnique(user.getPhoneNumber(),user.getUserType());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(CUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        CommonUser info = cUserExtMapper.checkEmailUnique(user.getEmail(),user.getUserType());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkAccountUnique(CUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        CommonUser info = cUserExtMapper.checkAccountUnique(user.getAccount(),user.getUserType());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public CommonUser selectCUserByAccount(String account,String userType) {
        return cUserExtMapper.selectCUserByAccount(account,userType);
    }
}
