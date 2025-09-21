package com.xingqiao.order.mapper.system;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xingqiao.order.domain.system.CustomerInfoExt;
import java.util.List;

/**
 * 系统信息Mapper接口
 *
 * @author xingqiao
 * @date 2025-09-21
 */
@DS("system")  // 指定使用system数据源
public interface SystemMapper
{
     List<CustomerInfoExt> selectCustomerInfoList(CustomerInfoExt customerInfo);

}
