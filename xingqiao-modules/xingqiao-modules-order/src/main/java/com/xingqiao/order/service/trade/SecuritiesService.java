package com.xingqiao.order.service.trade;

import java.util.List;
import com.xingqiao.common.core.utils.DateUtils;
import org.springframework.stereotype.Service;
import com.xingqiao.order.mapper.SecuritiesMapper;
import com.xingqiao.order.domain.Securities;

import javax.annotation.Resource;

/**
 * 证券信息Service业务层处理
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@Service
public class SecuritiesService
{
    @Resource
    private SecuritiesMapper securitiesMapper;

    /**
     * 查询证券信息
     *
     * @param id 证券信息主键
     * @return 证券信息
     */
    public Securities selectSecuritiesById(String id)
    {
        return securitiesMapper.selectSecuritiesById(id);
    }

    /**
     * 查询证券信息列表
     *
     * @param securities 证券信息
     * @return 证券信息集合
     */
    public List<Securities> selectSecuritiesList(Securities securities)
    {
        return securitiesMapper.selectSecuritiesList(securities);
    }

    /**
     * 新增证券信息
     *
     * @param securities 证券信息
     * @return 结果
     */
    public int insertSecurities(Securities securities)
    {
        securities.setCreateTime(DateUtils.getNowDate());
        return securitiesMapper.insertSecurities(securities);
    }

    /**
     * 修改证券信息
     *
     * @param securities 证券信息
     * @return 结果
     */
    public int updateSecurities(Securities securities)
    {
        securities.setUpdateTime(DateUtils.getNowDate());
        return securitiesMapper.updateSecurities(securities);
    }

    /**
     * 批量删除证券信息
     *
     * @param ids 需要删除的证券信息主键集合
     * @return 结果
     */
    public int deleteSecuritiesByIds(String[] ids)
    {
        return securitiesMapper.deleteSecuritiesByIds(ids);
    }

    /**
     * 删除证券信息信息
     *
     * @param id 证券信息主键
     * @return 结果
     */
    public int deleteSecuritiesById(String id)
    {
        return securitiesMapper.deleteSecuritiesById(id);
    }
}