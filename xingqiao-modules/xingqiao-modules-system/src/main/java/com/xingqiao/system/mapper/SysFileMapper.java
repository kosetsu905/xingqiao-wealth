package com.xingqiao.system.mapper;

import java.util.List;
import com.xingqiao.system.domain.SysFile;

/**
 * 系统文件管理Mapper接口
 *
 * @author xingqiao
 * @date 2025-08-17
 */
public interface SysFileMapper
{
    /**
     * 查询系统文件管理
     *
     * @param id 系统文件管理主键
     * @return 系统文件管理
     */
    public SysFile selectSysFileById(Long id);

    /**
     * 查询系统文件管理列表
     *
     * @param sysFile 系统文件管理
     * @return 系统文件管理集合
     */
    public List<SysFile> selectSysFileList(SysFile sysFile);

    /**
     * 新增系统文件管理
     *
     * @param sysFile 系统文件管理
     * @return 结果
     */
    public int insertSysFile(SysFile sysFile);

    /**
     * 修改系统文件管理
     *
     * @param sysFile 系统文件管理
     * @return 结果
     */
    public int updateSysFile(SysFile sysFile);

    /**
     * 删除系统文件管理
     *
     * @param id 系统文件管理主键
     * @return 结果
     */
    public int deleteSysFileById(Long id);

    /**
     * 批量删除系统文件管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysFileByIds(Long[] ids);
    
    /**
     * 批量新增系统文件管理
     * 
     * @param sysFileList 系统文件管理列表
     * @return 结果
     */
    public int batchSysFile(List<SysFile> sysFileList);
    
    /**
     * 批量更新系统文件管理
     * 
     * @param sysFileList 系统文件管理列表
     * @return 结果
     */
    public int batchUpdateSysFile(List<SysFile> sysFileList);
}