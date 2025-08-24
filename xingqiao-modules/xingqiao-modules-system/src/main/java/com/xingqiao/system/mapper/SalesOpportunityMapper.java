package com.xingqiao.system.mapper;

import java.util.List;
import com.xingqiao.system.domain.SalesOpportunity;

/**
 * 客户意向记录Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-23
 */
public interface SalesOpportunityMapper
{
    /**
     * 查询客户意向记录
     *
     * @param id 客户意向记录主键
     * @return 客户意向记录
     */
    public SalesOpportunity selectSalesOpportunityById(Long id);

    /**
     * 查询客户意向记录列表
     *
     * @param salesOpportunity 客户意向记录
     * @return 客户意向记录集合
     */
    public List<SalesOpportunity> selectSalesOpportunityList(SalesOpportunity salesOpportunity);

    /**
     * 新增客户意向记录
     *
     * @param salesOpportunity 客户意向记录
     * @return 结果
     */
    public int insertSalesOpportunity(SalesOpportunity salesOpportunity);

    /**
     * 修改客户意向记录
     *
     * @param salesOpportunity 客户意向记录
     * @return 结果
     */
    public int updateSalesOpportunity(SalesOpportunity salesOpportunity);

    /**
     * 删除客户意向记录
     *
     * @param id 客户意向记录主键
     * @return 结果
     */
    public int deleteSalesOpportunityById(Long id);

    /**
     * 批量删除客户意向记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSalesOpportunityByIds(Long[] ids);
}
