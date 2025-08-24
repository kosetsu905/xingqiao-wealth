package com.xingqiao.system.mapper;

import com.xingqiao.system.domain.SysEmployeeQualifications;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工资质认证Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public interface SysEmployeeQualificationsExtMapper
{

    /**
     * 删除员工资质认证
     *
     * @param id 员工资质认证主键
     * @return 结果
     */
    public int deleteByBusinessId(@Param("businessId") Long businessId);


}
