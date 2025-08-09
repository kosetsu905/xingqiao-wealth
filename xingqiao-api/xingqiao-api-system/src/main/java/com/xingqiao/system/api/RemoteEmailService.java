package com.xingqiao.system.api;

import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.common.core.constant.ServiceNameConstants;
import com.xingqiao.common.core.domain.R;
import com.xingqiao.system.api.factory.RemoteEmailFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 * 
 * @author xingqiao
 */
@FeignClient(contextId = "remoteEmailService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteEmailFallbackFactory.class)
public interface RemoteEmailService
{

    @PostMapping("/email/simple")
    public R sendSimpleEmail(@RequestParam("to") String to,
                             @RequestParam("subject") String subject,
                             @RequestParam("text") String text, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/email/html")
    public R sendHtmlEmail(@RequestParam("to") String to,
                           @RequestParam("subject") String subject,
                           @RequestParam("htmlContent") String htmlContent, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @PostMapping("/email/template")
    public R sendTemplateEmail(@RequestParam("to") String to,
                               @RequestParam("subject") String subject,
                               @RequestParam("templateName") String templateName, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}