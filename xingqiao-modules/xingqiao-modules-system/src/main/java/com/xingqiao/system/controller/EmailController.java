package com.xingqiao.system.controller;


import com.xingqiao.common.core.domain.R;
import com.xingqiao.common.core.web.controller.BaseController;
import com.xingqiao.system.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/email")
@Slf4j
public class EmailController extends BaseController{
    @Autowired
    private EmailUtils emailUtils;

    /**
     * 发送简单文本邮件测试
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param text 邮件正文
     * @return 操作结果
     */
    @PostMapping("/simple")
    public R sendSimpleEmail(@RequestParam String to,
                             @RequestParam String subject,
                             @RequestParam String text) {
        try {
            emailUtils.sendSimpleEmail(to, subject, text);
            return R.ok("简单文本邮件发送成功");
        } catch (Exception e) {
            log.error("发送简单文本邮件失败", e);
            return R.fail("发送简单文本邮件失败: " + e.getMessage());
        }
    }

    /**
     * 发送HTML邮件测试
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param htmlContent HTML内容
     * @return 操作结果
     */
    @PostMapping("/html")
    public R sendHtmlEmail(@RequestParam String to,
                           @RequestParam String subject,
                           @RequestParam String htmlContent) {
        try {
            emailUtils.sendHtmlEmail(to, subject, htmlContent, false);
            return R.ok("HTML邮件发送成功");
        } catch (Exception e) {
            log.error("发送HTML邮件失败", e);
            return R.fail("发送HTML邮件失败: " + e.getMessage());
        }
    }

    /**
     * 发送Thymeleaf模板邮件测试
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param templateName 模板名称
     * @return 操作结果
     */
    @PostMapping("/template")
    public R sendTemplateEmail(@RequestParam String to,
                               @RequestParam String subject,
                               @RequestParam String templateName) {
        try {
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("name", "测试用户");
            templateData.put("content", "这是一封使用Thymeleaf模板发送的测试邮件");
            templateData.put("date", new java.util.Date());

            emailUtils.sendThymeleafEmail(templateName, templateData, to, subject);
            return R.ok("模板邮件发送成功");
        } catch (Exception e) {
            log.error("发送模板邮件失败", e);
            return R.fail("发送模板邮件失败: " + e.getMessage());
        }
    }
}
