package com.xingqiao.order.service;

import java.util.List;
import com.xingqiao.order.domain.CustomerPositions;

/**
 * 客户持仓Service接口
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
public interface ICustomerPositionsService
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
     * 批量删除客户持仓
     * 
     * @param ids 需要删除的客户持仓主键集合
     * @return 结果
     */
    public int deleteCustomerPositionsByIds(String[] ids);

    /**
     * 删除客户持仓信息
     * 
     * @param id 客户持仓主键
     * @return 结果
     */
    public int deleteCustomerPositionsById(String id);

    /**
     * 根据用户ID查询客户持仓
     * 
     * @param userId 用户ID
     * @return 客户持仓集合
     */
    public List<CustomerPositions> selectCustomerPositionsByUserId(String userId);

    /**
     * 根据账户ID查询客户持仓
     * 
     * @param accountId 账户ID
     * @return 客户持仓集合
     */
    public List<CustomerPositions> selectCustomerPositionsByAccountId(String accountId);

    /**
     * 根据证券ID查询客户持仓
     * 
     * @param securityId 证券ID
     * @return 客户持仓集合
     */
    public List<CustomerPositions> selectCustomerPositionsBySecurityId(String securityId);

    /**
     * 根据账户ID、证券ID和持仓类型查询客户持仓
     * 
     * @param accountId 账户ID
     * @param securityId 证券ID
     * @param positionType 持仓类型
     * @return 客户持仓
     */
    public CustomerPositions selectCustomerPositionByAccountSecurityType(String accountId, String securityId, Long positionType);
}