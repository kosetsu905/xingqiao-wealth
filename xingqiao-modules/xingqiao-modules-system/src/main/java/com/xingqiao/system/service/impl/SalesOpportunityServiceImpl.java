package com.xingqiao.system.service.impl;

import java.util.List;
import com.xingqiao.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.SalesOpportunityMapper;
import com.xingqiao.system.domain.SalesOpportunity;
import com.xingqiao.system.service.ISalesOpportunityService;

/**
 * 客户意向记录Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-23
 */
@Service
public class SalesOpportunityServiceImpl implements ISalesOpportunityService
{
    @Autowired
    private SalesOpportunityMapper salesOpportunityMapper;

    /**
     * 查询客户意向记录
     *
     * @param id 客户意向记录主键
     * @return 客户意向记录
     */
    @Override
    public SalesOpportunity selectSalesOpportunityById(Long id)
    {
        return salesOpportunityMapper.selectSalesOpportunityById(id);
    }

    /**
     * 查询客户意向记录列表
     *
     * @param salesOpportunity 客户意向记录
     * @return 客户意向记录
     */
    @Override
    public List<SalesOpportunity> selectSalesOpportunityList(SalesOpportunity salesOpportunity)
    {
        return salesOpportunityMapper.selectSalesOpportunityList(salesOpportunity);
    }

    /**
     * 新增客户意向记录
     *
     * @param salesOpportunity 客户意向记录
     * @return 结果
     */
    @Override
    public int insertSalesOpportunity(SalesOpportunity salesOpportunity)
    {
        salesOpportunity.setCreateTime(DateUtils.getNowDate());
        return salesOpportunityMapper.insertSalesOpportunity(salesOpportunity);
    }

    /**
     * 修改客户意向记录
     *
     * @param salesOpportunity 客户意向记录
     * @return 结果
     */
    @Override
    public int updateSalesOpportunity(SalesOpportunity salesOpportunity)
    {
        salesOpportunity.setUpdateTime(DateUtils.getNowDate());
        return salesOpportunityMapper.updateSalesOpportunity(salesOpportunity);
    }

    /**
     * 批量删除客户意向记录
     *
     * @param ids 需要删除的客户意向记录主键
     * @return 结果
     */
    @Override
    public int deleteSalesOpportunityByIds(Long[] ids)
    {
        return salesOpportunityMapper.deleteSalesOpportunityByIds(ids);
    }

    /**
     * 删除客户意向记录信息
     *
     * @param id 客户意向记录主键
     * @return 结果
     */
    @Override
    public int deleteSalesOpportunityById(Long id)
    {
        return salesOpportunityMapper.deleteSalesOpportunityById(id);
    }
}
