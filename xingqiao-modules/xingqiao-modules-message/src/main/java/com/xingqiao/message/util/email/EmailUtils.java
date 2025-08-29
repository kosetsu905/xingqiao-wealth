package com.xingqiao.message.util.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送工具类（支持文本、HTML、附件、Thymeleaf模板）
 * 优化点：构造注入、参数校验、异常封装、日志记录
 */
@Component
@Slf4j // 自动注入日志
public class EmailUtils {


    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String fromEmail; // 发件人邮箱（从配置注入）

    /**
     * 构造注入（所有依赖通过构造函数传入，符合 Spring 最佳实践）
     * @param mailSender 邮件发送器（Spring 自动装配）
     * @param templateEngine Thymeleaf 模板引擎（Spring 自动装配）
     * @param fromEmail 发件人邮箱（从配置 ${spring.mail.username} 注入）
     */
    public EmailUtils(JavaMailSender mailSender,
                      TemplateEngine templateEngine,
                      @Value("${spring.mail.username}") String fromEmail) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.fromEmail = fromEmail;
        // 校验必要配置（避免启动时报错）
        Assert.hasText(fromEmail, "发件人邮箱（spring.mail.username）未配置");
        Assert.notNull(mailSender, "JavaMailSender 未正确注入");
    }

    // ------------------------------ 基础功能：发送简单文本邮件 ------------------------------
    /**
     * 发送纯文本邮件（参数校验 + 异常封装）
     * @param to 收件人邮箱（多个用逗号分隔，非空）
     * @param subject 邮件主题（非空）
     * @param text 邮件正文（纯文本，非空）
     * @throws EmailException 邮件发送异常（包装底层 MessagingException）
     */
    public void sendSimpleEmail(String to, String subject, String text) throws EmailException {
        // 参数校验
        Assert.hasText(to, "收件人邮箱（to）不能为空");
        Assert.hasText(subject, "邮件主题（subject）不能为空");
        Assert.hasText(text, "邮件正文（text）不能为空");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to.split(","));       // 收件人（支持多个）
            message.setSubject(subject);        // 主题
            message.setText(text);              // 正文
            message.setFrom(fromEmail);         // 显式设置发件人（覆盖默认配置）
            mailSender.send(message);
            log.info("简单文本邮件发送成功 | 收件人：{} | 主题：{}", to, subject);
        } catch (Exception e) {
            log.error("简单文本邮件发送失败 | 收件人：{} | 主题：{}", to, subject, e);
            throw new EmailException("简单文本邮件发送失败", e);
        }
    }

    // ------------------------------ 进阶功能：发送 HTML 邮件 ------------------------------
    /**
     * 发送 HTML 邮件（参数校验 + 异常封装）
     * @param to 收件人邮箱（多个用逗号分隔，非空）
     * @param subject 邮件主题（非空）
     * @param htmlContent HTML 正文内容（非空）
     * @param isMultipart 是否支持附件（true：支持 multipart 格式）
     * @throws EmailException 邮件发送异常（包装底层 MessagingException）
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent, boolean isMultipart) throws EmailException {
        // 参数校验
        Assert.hasText(to, "收件人邮箱（to）不能为空");
        Assert.hasText(subject, "邮件主题（subject）不能为空");
        Assert.hasText(htmlContent, "HTML 正文内容（htmlContent）不能为空");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            // MimeMessageHelper 第二个参数为 true 表示支持 multipart 格式（附件、内联资源）
            MimeMessageHelper helper = new MimeMessageHelper(message, isMultipart, "utf-8");

            helper.setTo(to.split(","));        // 收件人
            helper.setSubject(subject);         // 主题
            helper.setFrom(fromEmail);          // 发件人（使用配置的邮箱）
            helper.setText(htmlContent, true);  // 标记为 HTML 内容

            mailSender.send(message);
            log.info("HTML 邮件发送成功 | 收件人：{} | 主题：{}", to, subject);
        } catch (MessagingException e) {
            log.error("HTML 邮件发送失败 | 收件人：{} | 主题：{}", to, subject, e);
            throw new EmailException("HTML 邮件发送失败", e);
        }
    }

    // ------------------------------ 扩展功能：发送带附件的邮件 ------------------------------
    /**
     * 发送带附件的邮件（参数校验 + 异常封装）
     * @param to 收件人邮箱（多个用逗号分隔，非空）
     * @param subject 邮件主题（非空）
     * @param htmlContent HTML 正文内容（可选，若为 null 则发送纯文本）
     * @param textContent 纯文本正文（当 htmlContent 为 null 时使用，非空）
     * @param attachments 附件列表（文件路径数组，至少一个附件）
     * @throws EmailException 邮件发送异常（包装底层 MessagingException）
     */
    public void sendEmailWithAttachments(
            String to,
            String subject,
            String htmlContent,
            String textContent,
            List<String> attachments
    ) throws EmailException {
        // 参数校验
        Assert.hasText(to, "收件人邮箱（to）不能为空");
        Assert.hasText(subject, "邮件主题（subject）不能为空");
        Assert.isTrue(!attachments.isEmpty(), "附件列表（attachments）不能为空");
        if (htmlContent == null) {
            Assert.hasText(textContent, "纯文本正文（textContent）不能为空（当 htmlContent 为 null 时）");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8"); // 显式启用 multipart

            helper.setTo(to.split(","));
            helper.setSubject(subject);
            helper.setFrom(fromEmail);

            // 设置正文（优先 HTML，无则用纯文本）
            if (htmlContent != null) {
                helper.setText(htmlContent, true);
            } else {
                helper.setText(textContent);
            }

            // 添加附件（遍历文件路径）
            for (String filePath : attachments) {
                File file = new File(filePath);
                Assert.isTrue(file.exists(), "附件文件不存在：{}");
                MimeBodyPart attachmentPart = createAttachmentPart(file);
                helper.addAttachment(file.getName(), (DataSource) attachmentPart); // 更安全的方式添加附件
            }

            mailSender.send(message);
            log.info("带附件邮件发送成功 | 收件人：{} | 主题：{} | 附件数量：{}", to, subject, attachments.size());
        } catch (MessagingException | IOException e) {
            log.error("带附件邮件发送失败 | 收件人：{} | 主题：{}", to, subject, e);
            throw new EmailException("带附件邮件发送失败", e);
        }
    }

    /**
     * 创建附件部分（内部方法）
     */
    private MimeBodyPart createAttachmentPart(File file) throws MessagingException, IOException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(file);
        return attachmentPart;
    }

    // ------------------------------ 高级功能：发送 Thymeleaf 模板邮件 ------------------------------
    /**
     * 使用 Thymeleaf 模板生成邮件正文（HTML）
     * @param templateName 模板名称（如 "email/welcome"，对应 src/main/resources/templates/email/welcome.html）
     * @param templateData 模板变量（Map 类型，键值对，非空）
     * @param to 收件人邮箱（多个用逗号分隔，非空）
     * @param subject 邮件主题（非空）
     * @throws EmailException 邮件发送异常（包装底层异常）
     */
    public void sendThymeleafEmail(
            String templateName,
            Map<String, Object> templateData,
            String to,
            String subject
    ) throws EmailException {
        // 参数校验
        Assert.hasText(templateName, "模板名称（templateName）不能为空");
        Assert.notNull(templateData, "模板变量（templateData）不能为空");
        Assert.hasText(to, "收件人邮箱（to）不能为空");
        Assert.hasText(subject, "邮件主题（subject）不能为空");

        try {
            // 1. 渲染 Thymeleaf 模板
            Context context = new Context();
            context.setVariables(templateData);
            String htmlContent = templateEngine.process(templateName, context);
            Assert.hasText(htmlContent, "Thymeleaf 模板渲染结果为空（模板名称或变量可能错误）");

            // 2. 发送 HTML 邮件（复用 sendHtmlEmail 方法）
            sendHtmlEmail(to, subject, htmlContent, true);
            log.info("Thymeleaf 模板邮件发送成功 | 模板：{} | 收件人：{} | 主题：{}", templateName, to, subject);
        } catch (Exception e) { // 捕获模板渲染异常和邮件发送异常
            log.error("Thymeleaf 模板邮件发送失败 | 模板：{} | 收件人：{} | 主题：{}", templateName, to, subject, e);
            throw new EmailException("Thymeleaf 模板邮件发送失败", e);
        }
    }

    /**
     * 自定义邮件异常类（封装底层异常）
     */
    public static class EmailException extends Exception {
        public EmailException(String message) {
            super(message);
        }

        public EmailException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}