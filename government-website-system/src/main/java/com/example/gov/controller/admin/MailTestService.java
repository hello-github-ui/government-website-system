package com.example.gov.controller.admin;

import com.example.gov.common.BizException;
import com.example.gov.service.SettingService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.time.LocalDateTime;
import java.util.Properties;

/**
 * SMTP 邮件发送测试工具。
 *
 * <p>根据系统设置中保存的 smtp_* 配置动态构造 JavaMailSenderImpl 并发送一封测试邮件，
 * 用于验证阿里云企业邮箱等 SMTP 配置是否正确。</p>
 */
public final class MailTestService {

    private MailTestService() {
    }

    /**
     * 发送测试邮件到指定邮箱。
     *
     * @param settingService 设置服务（读取 smtp_* 配置）
     * @param to             接收测试邮件的邮箱地址
     */
    public static void sendTest(SettingService settingService, String to) {
        String host = settingService.get("smtp_host", "");
        String port = settingService.get("smtp_port", "465");
        String user = settingService.get("smtp_user", "");
        String pass = settingService.get("smtp_pass", "");
        String from = settingService.get("smtp_from", "");
        if (host.isBlank()) {
            throw new BizException("请先在系统设置中配置 SMTP 服务器地址");
        }
        if (to == null || to.isBlank()) {
            throw new BizException("请填写接收测试邮件的邮箱地址");
        }

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(Integer.parseInt(port));
        sender.setUsername(user);
        sender.setPassword(pass);
        sender.setDefaultEncoding("UTF-8");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        boolean ssl = "ssl".equalsIgnoreCase(settingService.get("smtp_secure", "ssl"));
        props.put("mail.smtp.ssl.enable", String.valueOf(ssl));
        if (!ssl) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.connectiontimeout", "10000");
        sender.setJavaMailProperties(props);

        try {
            MimeMessage msg = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(from.isBlank() ? user : from);
            helper.setTo(to);
            helper.setSubject("政府官网系统 - SMTP 配置测试邮件");
            helper.setText("这是一封来自政府官网系统的测试邮件，说明 SMTP 邮件配置生效。\n\n发送时间: " + LocalDateTime.now(), true);
            sender.send(msg);
        } catch (Exception e) {
            throw new BizException("邮件发送失败: " + e.getMessage());
        }
    }
}
