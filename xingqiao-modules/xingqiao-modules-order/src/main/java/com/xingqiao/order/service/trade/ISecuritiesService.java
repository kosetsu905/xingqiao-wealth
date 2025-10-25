package com.xingqiao.order.service.trade;


import java.util.List;
import com.xingqiao.order.domain.Securities;

/**
 * 证券信息Service接口
 *
 * @author xingqiao
 * @date 2025-09-21
 */
public interface ISecuritiesService
{
    /**
     * 查询证券信息
     *
     * @param id 证券信息主键
     * @return 证券信息
     */
    public Securities selectSecuritiesById(String id);

    /**
     * 查询证券信息列表
     *
     * @param securities 证券信息
     * @return 证券信息集合
     */
    public List<Securities> selectSecuritiesList(Securities securities);

    /**
     * 新增证券信息
     *
     * @param securities 证券信息
     * @return 结果
     */
    public int insertSecurities(Securities securities);

    /**
     * 修改证券信息
     *
     * @param securities 证券信息
     * @return 结果
     */
    public int updateSecurities(Securities securities);

    /**
     * 批量删除证券信息
     *
     * @param ids 需要删除的证券信息主键集合
     * @return 结果
     */
    public int deleteSecuritiesByIds(String[] ids);

    /**
     * 删除证券信息信息
     *
     * @param id 证券信息主键
     * @return 结果
     */
    public int deleteSecuritiesById(String id);
}
