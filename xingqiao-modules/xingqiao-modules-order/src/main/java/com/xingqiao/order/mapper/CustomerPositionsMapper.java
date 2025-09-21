package com.xingqiao.order.mapper;

import java.util.List;
import com.xingqiao.order.domain.CustomerPositions;

/**
 * 客户持仓Mapper接口
 *
 * @author xingqiao
 * @date 2025-09-21
 */
public interface CustomerPositionsMapper
{
    /**
     * 查询客户持仓
     *
     * @param id 客户持仓主键
     * @return 客户持仓
     */
    public CustomerPositions selectCustomerPositionsById(String id);

    /**
     * 查询客户持仓列表
     *
     * @param customerPositions 客户持仓
     * @return 客户持仓集合
     */
    public List<CustomerPositions> selectCustomerPositionsList(CustomerPositions customerPositions);

    /**
     * 新增客户持仓
     *
     * @param customerPositions 客户持仓
     * @return 结果
     */
    public int insertCustomerPositions(CustomerPositions customerPositions);

    /**
     * 修改客户持仓
     *
     * @param customerPositions 客户持仓
     * @return 结果
     */
    public int updateCustomerPositions(CustomerPositions customerPositions);

    /**
     * 删除客户持仓
     *
     * @param id 客户持仓主键
     * @return 结果
     */
    public int deleteCustomerPositionsById(String id);

    /**
     * 批量删除客户持仓
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomerPositionsByIds(String[] ids);
}
