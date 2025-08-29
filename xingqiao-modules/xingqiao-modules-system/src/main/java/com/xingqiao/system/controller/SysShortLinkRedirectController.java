package com.xingqiao.system.controller;

import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.system.domain.SysShortLink;
import com.xingqiao.system.service.ISysShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短链接跳转Controller
 * 
 * @author xingqiao
 */
@Controller
@RequestMapping("/s")
public class SysShortLinkRedirectController extends BaseController {

    @Autowired
    private ISysShortLinkService sysShortLinkService;

    /**
     * 短链接跳转
     *
     * @param shortCode 短链接码
     * @param response 响应对象
     */
    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        // 查询短链接信息
        SysShortLink shortLink = sysShortLinkService.selectSysShortLinkByShortCode(shortCode);

        // 判断短链接是否存在且启用
        if (shortLink != null && "0".equals(shortLink.getStatus())) {
            // 增加点击次数
            shortLink.setClickCount(shortLink.getClickCount() + 1);
            sysShortLinkService.updateSysShortLink(shortLink);

            // 重定向到原始链接
            response.sendRedirect(shortLink.getOriginalUrl());
        } else {
            // 短链接不存在或已停用，返回404
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}