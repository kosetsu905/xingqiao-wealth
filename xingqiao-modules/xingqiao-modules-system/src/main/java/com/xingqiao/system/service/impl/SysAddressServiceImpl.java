package com.xingqiao.system.service.impl;

import java.util.List;
import com.xingqiao.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.SysAddressMapper;
import com.xingqiao.system.domain.SysAddress;
import com.xingqiao.system.service.ISysAddressService;

/**
 * 系统地址簿Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-23
 */
@Service
public class SysAddressServiceImpl implements ISysAddressService
{
    @Autowired
    private SysAddressMapper sysAddressMapper;

    /**
     * 查询系统地址簿
     *
     * @param id 系统地址簿主键
     * @return 系统地址簿
     */
    @Override
    public SysAddress selectSysAddressById(Long id)
    {
        return sysAddressMapper.selectSysAddressById(id);
    }

    /**
     * 查询系统地址簿列表
     *
     * @param sysAddress 系统地址簿
     * @return 系统地址簿
     */
    @Override
    public List<SysAddress> selectSysAddressList(SysAddress sysAddress)
    {
        return sysAddressMapper.selectSysAddressList(sysAddress);
    }

    /**
     * 新增系统地址簿
     *
     * @param sysAddress 系统地址簿
     * @return 结果
     */
    @Override
    public int insertSysAddress(SysAddress sysAddress)
    {
        sysAddress.setCreateTime(DateUtils.getNowDate());
        return sysAddressMapper.insertSysAddress(sysAddress);
    }

    /**
     * 修改系统地址簿
     *
     * @param sysAddress 系统地址簿
     * @return 结果
     */
    @Override
    public int updateSysAddress(SysAddress sysAddress)
    {
        sysAddress.setUpdateTime(DateUtils.getNowDate());
        return sysAddressMapper.updateSysAddress(sysAddress);
    }

    /**
     * 批量删除系统地址簿
     *
     * @param ids 需要删除的系统地址簿主键
     * @return 结果
     */
    @Override
    public int deleteSysAddressByIds(Long[] ids)
    {
        return sysAddressMapper.deleteSysAddressByIds(ids);
    }

    /**
     * 删除系统地址簿信息
     *
     * @param id 系统地址簿主键
     * @return 结果
     */
    @Override
    public int deleteSysAddressById(Long id)
    {
        return sysAddressMapper.deleteSysAddressById(id);
    }
}
