package com.xingqiao.order.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.order.domain.CustomerPositions;
import com.xingqiao.order.mapper.CustomerPositionsMapper;
import com.xingqiao.order.service.ICustomerPositionsService;

/**
 * 客户持仓Service业务层处理
 * 
 * @author xingqiao
 * @date 2025-09-21
 */
@Service
public class CustomerPositionsServiceImpl implements ICustomerPositionsService
{
    @Autowired
    private CustomerPositionsMapper customerPositionsMapper;

    /**
     * 查询客户持仓
     * 
     * @param id 客户持仓主键
     * @return 客户持仓
     */
    @Override
    public CustomerPositions selectCustomerPositionsById(String id)
    {
        return customerPositionsMapper.selectCustomerPositionsById(id);
    }

    /**
     * 查询客户持仓列表
     * 
     * @param customerPositions 客户持仓
     * @return 客户持仓集合
     */
    @Override
    public List<CustomerPositions> selectCustomerPositionsList(CustomerPositions customerPositions)
    {
        return customerPositionsMapper.selectCustomerPositionsList(customerPositions);
    }

    /**
     * 新增客户持仓
     * 
     * @param customerPositions 客户持仓
     * @return 结果
     */
    @Override
    public int insertCustomerPositions(CustomerPositions customerPositions)
    {
        customerPositions.setCreateTime(DateUtils.getNowDate());
        return customerPositionsMapper.insertCustomerPositions(customerPositions);
    }

    /**
     * 修改客户持仓
     * 
     * @param customerPositions 客户持仓
     * @return 结果
     */
    @Override
    public int updateCustomerPositions(CustomerPositions customerPositions)
    {
        customerPositions.setUpdateTime(DateUtils.getNowDate());
        return customerPositionsMapper.updateCustomerPositions(customerPositions);
    }

    /**
     * 批量删除客户持仓
     * 
     * @param ids 需要删除的客户持仓主键集合
     * @return 结果
     */
    @Override
    public int deleteCustomerPositionsByIds(String[] ids)
    {
        return customerPositionsMapper.deleteCustomerPositionsByIds(ids);
    }

    /**
     * 删除客户持仓信息
     * 
     * @param id 客户持仓主键
     * @return 结果
     */
    @Override
    public int deleteCustomerPositionsById(String id)
    {
        return customerPositionsMapper.deleteCustomerPositionsById(id);
    }

    /**
     * 根据用户ID查询客户持仓
     * 
     * @param userId 用户ID
     * @return 客户持仓集合
     */
    @Override
    public List<CustomerPositions> selectCustomerPositionsByUserId(String userId)
    {
        CustomerPositions customerPositions = new CustomerPositions();
        customerPositions.setUserId(userId);
        return customerPositionsMapper.selectCustomerPositionsList(customerPositions);
    }

    /**
     * 根据账户ID查询客户持仓
     * 
     * @param accountId 账户ID
     * @return 客户持仓集合
     */
    @Override
    public List<CustomerPositions> selectCustomerPositionsByAccountId(String accountId)
    {
        CustomerPositions customerPositions = new CustomerPositions();
        customerPositions.setAccountId(accountId);
        return customerPositionsMapper.selectCustomerPositionsList(customerPositions);
    }

    /**
     * 根据证券ID查询客户持仓
     * 
     * @param securityId 证券ID
     * @return 客户持仓集合
     */
    @Override
    public List<CustomerPositions> selectCustomerPositionsBySecurityId(String securityId)
    {
        CustomerPositions customerPositions = new CustomerPositions();
        customerPositions.setSecurityId(securityId);
        return customerPositionsMapper.selectCustomerPositionsList(customerPositions);
    }

    /**
     * 根据账户ID、证券ID和持仓类型查询客户持仓
     * 
     * @param accountId 账户ID
     * @param securityId 证券ID
     * @param positionType 持仓类型
     * @return 客户持仓
     */
    @Override
    public CustomerPositions selectCustomerPositionByAccountSecurityType(String accountId, String securityId, Long positionType)
    {
        CustomerPositions customerPositions = new CustomerPositions();
        customerPositions.setAccountId(accountId);
        customerPositions.setSecurityId(securityId);
        customerPositions.setPositionType(positionType);
        List<CustomerPositions> list = customerPositionsMapper.selectCustomerPositionsList(customerPositions);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }
    
    /**
     * 根据账户ID计算该账户下所有股票持仓的价值总和
     * 
     * @param accountId 账户ID
     * @return 持仓价值总和
     */
    @Override
    public BigDecimal calculateTotalPositionsValueByAccountId(String accountId) {
        // 获取该账户下的所有持仓
        List<CustomerPositions> positionsList = selectCustomerPositionsByAccountId(accountId);
        
        // 初始化总和为0
        BigDecimal totalValue = BigDecimal.ZERO;
        
        // 遍历持仓列表，累加每个持仓的当前市值
        if (positionsList != null && !positionsList.isEmpty()) {
            for (CustomerPositions position : positionsList) {
                if (position.getMarketValue() != null) {
                    totalValue = totalValue.add(position.getMarketValue());
                }
            }
        }
        
        return totalValue;
    }
}