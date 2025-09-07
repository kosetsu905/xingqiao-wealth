package com.xingqiao.system.service.impl.employee;

import java.util.List;
import com.xingqiao.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.employee.SysCustomerFinancialMapper;
import com.xingqiao.system.domain.employee.SysCustomerFinancial;
import com.xingqiao.system.service.employee.ISysCustomerFinancialService;

/**
 * 客户财务信息Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-23
 */
@Service
public class SysCustomerFinancialServiceImpl implements ISysCustomerFinancialService
{
    @Autowired
    private SysCustomerFinancialMapper sysCustomerFinancialMapper;

    /**
     * 查询客户财务信息
     *
     * @param id 客户财务信息主键
     * @return 客户财务信息
     */
    @Override
    public SysCustomerFinancial selectSysCustomerFinancialById(Long id)
    {
        return sysCustomerFinancialMapper.selectSysCustomerFinancialById(id);
    }

    /**
     * 查询客户财务信息列表
     *
     * @param sysCustomerFinancial 客户财务信息
     * @return 客户财务信息
     */
    @Override
    public List<SysCustomerFinancial> selectSysCustomerFinancialList(SysCustomerFinancial sysCustomerFinancial)
    {
        return sysCustomerFinancialMapper.selectSysCustomerFinancialList(sysCustomerFinancial);
    }

    /**
     * 新增客户财务信息
     *
     * @param sysCustomerFinancial 客户财务信息
     * @return 结果
     */
    @Override
    public int insertSysCustomerFinancial(SysCustomerFinancial sysCustomerFinancial)
    {
        sysCustomerFinancial.setCreateTime(DateUtils.getNowDate());
        return sysCustomerFinancialMapper.insertSysCustomerFinancial(sysCustomerFinancial);
    }

    /**
     * 修改客户财务信息
     *
     * @param sysCustomerFinancial 客户财务信息
     * @return 结果
     */
    @Override
    public int updateSysCustomerFinancial(SysCustomerFinancial sysCustomerFinancial)
    {
        sysCustomerFinancial.setUpdateTime(DateUtils.getNowDate());
        return sysCustomerFinancialMapper.updateSysCustomerFinancial(sysCustomerFinancial);
    }

    /**
     * 批量删除客户财务信息
     *
     * @param ids 需要删除的客户财务信息主键
     * @return 结果
     */
    @Override
    public int deleteSysCustomerFinancialByIds(Long[] ids)
    {
        return sysCustomerFinancialMapper.deleteSysCustomerFinancialByIds(ids);
    }

    /**
     * 删除客户财务信息信息
     *
     * @param id 客户财务信息主键
     * @return 结果
     */
    @Override
    public int deleteSysCustomerFinancialById(Long id)
    {
        return sysCustomerFinancialMapper.deleteSysCustomerFinancialById(id);
    }
}
