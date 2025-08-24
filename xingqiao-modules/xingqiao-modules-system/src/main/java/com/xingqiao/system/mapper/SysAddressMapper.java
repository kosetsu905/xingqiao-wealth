package com.xingqiao.system.mapper;

import java.util.List;
import com.xingqiao.system.domain.SysAddress;

/**
 * 系统地址簿Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-23
 */
public interface SysAddressMapper
{
    /**
     * 查询系统地址簿
     *
     * @param id 系统地址簿主键
     * @return 系统地址簿
     */
    public SysAddress selectSysAddressById(Long id);

    /**
     * 查询系统地址簿列表
     *
     * @param sysAddress 系统地址簿
     * @return 系统地址簿集合
     */
    public List<SysAddress> selectSysAddressList(SysAddress sysAddress);

    /**
     * 新增系统地址簿
     *
     * @param sysAddress 系统地址簿
     * @return 结果
     */
    public int insertSysAddress(SysAddress sysAddress);

    /**
     * 修改系统地址簿
     *
     * @param sysAddress 系统地址簿
     * @return 结果
     */
    public int updateSysAddress(SysAddress sysAddress);

    /**
     * 删除系统地址簿
     *
     * @param id 系统地址簿主键
     * @return 结果
     */
    public int deleteSysAddressById(Long id);

    /**
     * 批量删除系统地址簿
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAddressByIds(Long[] ids);
}

