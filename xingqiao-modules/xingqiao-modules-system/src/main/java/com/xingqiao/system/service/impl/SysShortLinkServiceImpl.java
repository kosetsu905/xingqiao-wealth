package com.xingqiao.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xingqiao.system.mapper.SysShortLinkMapper;
import com.xingqiao.system.domain.SysShortLink;
import com.xingqiao.system.service.ISysShortLinkService;

/**
 * 短链接Service业务层处理
 * 
 * @author xingqiao
 */
@Service
public class SysShortLinkServiceImpl implements ISysShortLinkService 
{
    @Autowired
    private SysShortLinkMapper sysShortLinkMapper;

    /**
     * 查询短链接
     * 
     * @param shortLinkId 短链接主键
     * @return 短链接
     */
    @Override
    public SysShortLink selectSysShortLinkByShortLinkId(Long shortLinkId)
    {
        return sysShortLinkMapper.selectSysShortLinkByShortLinkId(shortLinkId);
    }

    /**
     * 根据短链接码查询短链接
     *
     * @param shortCode 短链接码
     * @return 短链接
     */
    @Override
    public SysShortLink selectSysShortLinkByShortCode(String shortCode)
    {
        return sysShortLinkMapper.selectSysShortLinkByShortCode(shortCode);
    }

    /**
     * 查询短链接列表
     * 
     * @param sysShortLink 短链接
     * @return 短链接
     */
    @Override
    public List<SysShortLink> selectSysShortLinkList(SysShortLink sysShortLink)
    {
        return sysShortLinkMapper.selectSysShortLinkList(sysShortLink);
    }

    /**
     * 新增短链接
     * 
     * @param sysShortLink 短链接
     * @return 结果
     */
    @Override
    public int insertSysShortLink(SysShortLink sysShortLink)
    {
        return sysShortLinkMapper.insertSysShortLink(sysShortLink);
    }

    /**
     * 修改短链接
     * 
     * @param sysShortLink 短链接
     * @return 结果
     */
    @Override
    public int updateSysShortLink(SysShortLink sysShortLink)
    {
        return sysShortLinkMapper.updateSysShortLink(sysShortLink);
    }

    /**
     * 批量删除短链接
     * 
     * @param shortLinkIds 需要删除的短链接主键
     * @return 结果
     */
    @Override
    public int deleteSysShortLinkByShortLinkIds(Long[] shortLinkIds)
    {
        return sysShortLinkMapper.deleteSysShortLinkByShortLinkIds(shortLinkIds);
    }

    /**
     * 删除短链接信息
     * 
     * @param shortLinkId 短链接主键
     * @return 结果
     */
    @Override
    public int deleteSysShortLinkByShortLinkId(Long shortLinkId)
    {
        return sysShortLinkMapper.deleteSysShortLinkByShortLinkId(shortLinkId);
    }
}