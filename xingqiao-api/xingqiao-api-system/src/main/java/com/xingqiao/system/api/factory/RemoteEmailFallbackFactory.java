package com.xingqiao.system.api.factory;

import com.xingqiao.common.core.domain.R;
import com.xingqiao.system.api.RemoteEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 * 
 * @author xingqiao
 */
@Component
public class RemoteEmailFallbackFactory implements FallbackFactory<RemoteEmailService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteEmailFallbackFactory.class);

    @Override
    public RemoteEmailService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteEmailService()
        {

            @Override
            public R sendSimpleEmail(String to, String subject, String text, String source) {
                return R.fail("发送邮件:" + throwable.getMessage());
            }

            @Override
            public R sendHtmlEmail(String to, String subject, String htmlContent, String source) {
                return R.fail("发送邮件:" + throwable.getMessage());
            }

            @Override
            public R sendTemplateEmail(String to, String subject, String templateName, String source) {
                return R.fail("发送邮件:" + throwable.getMessage());
            }
        };
    }
}
