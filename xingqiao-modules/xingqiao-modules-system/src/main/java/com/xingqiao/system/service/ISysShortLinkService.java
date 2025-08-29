package com.xingqiao.system.service;

import java.util.List;
import com.xingqiao.system.domain.SysShortLink;

/**
 * 短链接Service接口
 * 
 * @author xingqiao
 */
public interface ISysShortLinkService 
{
    /**
     * 查询短链接
     * 
     * @param shortLinkId 短链接主键
     * @return 短链接
     */
    public SysShortLink selectSysShortLinkByShortLinkId(Long shortLinkId);

    /**
     * 根据短链接码查询短链接
     *
     * @param shortCode 短链接码
     * @return 短链接
     */
    public SysShortLink selectSysShortLinkByShortCode(String shortCode);

    /**
     * 查询短链接列表
     * 
     * @param sysShortLink 短链接
     * @return 短链接集合
     */
    public List<SysShortLink> selectSysShortLinkList(SysShortLink sysShortLink);

    /**
     * 新增短链接
     * 
     * @param sysShortLink 短链接
     * @return 结果
     */
    public int insertSysShortLink(SysShortLink sysShortLink);

    /**
     * 修改短链接
     * 
     * @param sysShortLink 短链接
     * @return 结果
     */
    public int updateSysShortLink(SysShortLink sysShortLink);

    /**
     * 批量删除短链接
     * 
     * @param shortLinkIds 需要删除的短链接主键集合
     * @return 结果
     */
    public int deleteSysShortLinkByShortLinkIds(Long[] shortLinkIds);

    /**
     * 删除短链接信息
     * 
     * @param shortLinkId 短链接主键
     * @return 结果
     */
    public int deleteSysShortLinkByShortLinkId(Long shortLinkId);
}