package com.example.gov.service;

import com.example.gov.common.BizException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 邮件发送服务。
 *
 * <p>使用 Spring Boot 自动装配的 {@link JavaMailSender}（配置来源：application.yml 的
 * spring.mail.*，支持 QQ 邮箱 / 阿里云企业邮箱等 SMTP）。支持 HTML 富文本邮件，
 * 当前用于后台设置页发送邮件（内容可在设置页富文本编辑）。</p>
 */
@Slf4j
@Service
public class MailService {

    private final JavaMailSender mailSender;

    /**
     * SMTP 认证账号（QQ 邮箱要求发件人地址必须与认证账号一致）。
     */
    @Value("${spring.mail.username}")
    private String mailUsername;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 向指定邮箱发送一封 HTML 富文本邮件，验证 SMTP 配置是否生效。
     *
     * @param to      接收邮件地址
     * @param subject 邮件主题
     * @param html    邮件正文（HTML 富文本）
     */
    public void sendMail(String to, String subject, String html) {
        String thread = Thread.currentThread().getName();
        if (to == null || to.isBlank()) {
            log.warn("[{}] 邮件发送失败(收件人为空)", thread);
            throw new BizException("请填写收件邮箱地址");
        }
        if (subject == null || subject.isBlank()) {
            subject = "政府官网系统邮件";
        }
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(mailUsername);
            helper.setTo(to);
            helper.setSubject(subject);
            String body = (html == null || html.isBlank()) ? "<p>（邮件内容为空）</p>" : html;
            helper.setText(body, true);
            mailSender.send(msg);
            log.info("[{}] 邮件发送成功 to={}, subject={}, 正文长度={}",
                thread, to, subject, body.length());
        } catch (Exception e) {
            log.warn("[{}] 邮件发送失败 to={}, subject={}, 原因={}", thread, to, subject, e.getMessage());
            throw new BizException("邮件发送失败: " + e.getMessage());
        }
    }
}
