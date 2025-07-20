package com.xingqiao.system.mapper;

import com.xingqiao.system.api.domain.CUser;

import java.util.List;

/**
 * 客户信息Mapper接口
 *
 * @author xingqiao
 * @date 2025-07-19
 */
public interface CUserMapper
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
     * 删除客户信息
     *
     * @param userId 客户信息主键
     * @return 结果
     */
    public int deleteCUserByUserId(Long userId);

    /**
     * 批量删除客户信息
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCUserByUserIds(Long[] userIds);
}
