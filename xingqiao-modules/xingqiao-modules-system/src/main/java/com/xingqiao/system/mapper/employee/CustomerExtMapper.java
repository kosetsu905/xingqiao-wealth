package com.xingqiao.system.mapper.employee;


import com.xingqiao.system.api.model.CustomerListInnerResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户信息Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-23
 */

public interface CustomerExtMapper
{

    /**
     * 删除客户投资偏好
     *
     * @param userTempId
     * @return 结果
     */
     int deleteInvestmentPreferenceByUserId(@Param("userTempId") String userTempId);

    int deleteCustomerInfo(@Param("userTempId") String userTempId);

    void deleteSalesOpportunityByUserTempId(@Param("userTempId") String userTempId);

    void deleteCustomerFinancialByUserId(@Param("userTempId") String userTempId);

    List<CustomerListInnerResponse> selectSysCustomerInfoByIds(@Param("list") List<Long> idList);
}