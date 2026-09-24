package com.example.gov.service;

import com.example.gov.common.BizException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 邮件发送服务。
 *
 * <p>使用 Spring Boot 自动装配的 {@link JavaMailSender}（配置来源：application.yml 的
 * spring.mail.*，支持 QQ 邮箱 / 阿里云企业邮箱等 SMTP）。当前用于后台“发送测试邮件”，
 * 后续可扩展为注册通知、咨询回复通知等。</p>
 */
@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 向指定邮箱发送一封测试邮件，验证 SMTP 配置是否生效。
     *
     * @param to 接收测试邮件的邮箱地址
     */
    public void sendTestMail(String to) {
        if (to == null || to.isBlank()) {
            throw new BizException("请填写接收测试邮件的邮箱地址");
        }
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("政府官网系统 - SMTP 配置测试邮件");
            helper.setText("这是一封来自政府官网系统的测试邮件，说明 SMTP 邮件配置生效。\n\n发送时间: " + LocalDateTime.now(), true);
            mailSender.send(msg);
        } catch (Exception e) {
            throw new BizException("邮件发送失败: " + e.getMessage());
        }
    }
}
