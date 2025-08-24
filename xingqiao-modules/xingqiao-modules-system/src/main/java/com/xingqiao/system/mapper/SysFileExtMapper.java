package com.xingqiao.system.mapper;

import java.util.List;
import java.util.Set;

import com.xingqiao.system.domain.SysFile;
import org.apache.ibatis.annotations.Param;

/**
 * 系统文件管理扩展Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public interface SysFileExtMapper {

    int deleteSysFileByBusinessId(@Param("list") Set<Long> list);
    
    /**
     * 批量新增系统文件管理
     * 
     * @param sysFileList 系统文件管理列表
     * @return 结果
     */
    int batchSysFile(@Param("list") List<SysFile> sysFileList);
}