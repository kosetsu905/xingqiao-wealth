package com.xingqiao.system.service.impl;

import java.util.List;
import java.util.Set;

import com.xingqiao.common.core.utils.DateUtils;
import com.xingqiao.system.mapper.SysFileExtMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.SysFileMapper;
import com.xingqiao.system.domain.SysFile;
import com.xingqiao.system.service.ISysFileService;

import javax.annotation.Resource;

/**
 * 系统文件管理Service业务层处理
 *
 * @author xingqiao
 * @date 2025-08-17
 */
@Service
public class SysFileServiceImpl implements ISysFileService
{
    @Resource
    private SysFileMapper sysFileMapper;
    @Resource
    private SysFileExtMapper sysFileExtMapper;

    /**
     * 查询系统文件管理
     *
     * @param id 系统文件管理主键
     * @return 系统文件管理
     */
    @Override
    public SysFile selectSysFileById(Long id)
    {
        return sysFileMapper.selectSysFileById(id);
    }

    /**
     * 查询系统文件管理列表
     *
     * @param sysFile 系统文件管理
     * @return 系统文件管理
     */
    @Override
    public List<SysFile> selectSysFileList(SysFile sysFile)
    {
        return sysFileMapper.selectSysFileList(sysFile);
    }

    /**
     * 新增系统文件管理
     *
     * @param sysFile 系统文件管理
     * @return 结果
     */
    @Override
    public int insertSysFile(SysFile sysFile)
    {
        sysFile.setCreateTime(DateUtils.getNowDate());
        return sysFileMapper.insertSysFile(sysFile);
    }

    /**
     * 修改系统文件管理
     *
     * @param sysFile 系统文件管理
     * @return 结果
     */
    @Override
    public int updateSysFile(SysFile sysFile)
    {
        sysFile.setUpdateTime(DateUtils.getNowDate());
        return sysFileMapper.updateSysFile(sysFile);
    }

    /**
     * 批量删除系统文件管理
     *
     * @param ids 需要删除的系统文件管理主键
     * @return 结果
     */
    @Override
    public int deleteSysFileByIds(Long[] ids)
    {
        return sysFileMapper.deleteSysFileByIds(ids);
    }

    /**
     * 删除系统文件管理信息
     *
     * @param id 系统文件管理主键
     * @return 结果
     */
    @Override
    public int deleteSysFileById(Long id)
    {
        return sysFileMapper.deleteSysFileById(id);
    }

    @Override
    public int deleteSysFileByBusinessId(Set<Long> qualificationTypes) {
       return sysFileExtMapper.deleteSysFileByBusinessId(qualificationTypes);
    }

    @Override
    public int batchSysFile(List<SysFile> sysFiles) {
        return sysFileExtMapper.batchSysFile(sysFiles);
    }
}
