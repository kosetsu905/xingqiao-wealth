package com.xingqiao.auth.util;

import com.xingqiao.common.core.constant.SecurityConstants;
import com.xingqiao.system.api.RemoteEmailService;
import com.xingqiao.common.core.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import javax.annotation.Resource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 模板邮件工具类
 */
@Slf4j
@Component
public class TemplateEmailUtil {

    @Resource
    private RemoteEmailService remoteEmailService;

    /**
     * 读取HTML模板文件内容
     *
     * @param templatePath 模板路径，相对于resources目录
     * @return 模板文件内容
     */
    public String readTemplateToString(String templatePath) {
        try {
            ClassPathResource resource = new ClassPathResource(templatePath);
            InputStream inputStream = resource.getInputStream();
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("读取模板文件失败: {}", templatePath, e);
            throw new RuntimeException("读取模板文件失败");
        }
    }

    /**
     * 发送注册验证码邮件
     *
     * @param to      收件人邮箱
     * @param code    验证码
     * @return 发送结果
     */
    public R sendRegisterCodeEmail(String to, String code) {
        try {
            // 读取模板文件
            String templateContent = readTemplateToString("template/registerCodeTemplate.html");
            if (templateContent == null) {
                return R.fail("读取邮件模板失败");
            }

            // 替换模板中的验证码
            String emailContent = replaceTemplateVariable(templateContent, code);

            // 发送HTML邮件
            return remoteEmailService.sendHtmlEmail(to, "星桥理财助手 - 注册验证码", emailContent, SecurityConstants.FROM_SOURCE);
        } catch (Exception e) {
            log.error("发送注册验证码邮件失败", e);
            return R.fail("发送邮件失败: " + e.getMessage());
        }
    }

    /**
     * 替换模板中的变量
     *
     * @param templateContent 模板内容
     * @param code            验证码
     * @return 替换后的HTML内容
     */
    private String replaceTemplateVariable(String templateContent, String code) {
        // 使用简单的字符串替换
        return templateContent.replace("{code}", code);
    }
}