package com.example.gov.config;

import com.example.gov.common.SessionKeys;
import com.example.gov.service.LanguageService;
import com.example.gov.service.SettingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

/**
 * 全局视图数据注入。
 *
 * <p>前台每个页面自动注入站点配置(siteConfig)、通用文字(lang)、当前用户信息，
 * 等价于 PHP 基类 Controller::fetch() 中自动加载 siteConfig 的行为。</p>
 */
@ControllerAdvice(basePackages = "com.example.gov.controller.home")
public class HomeModelAdvice {

    private final SettingService settingService;
    private final LanguageService languageService;

    public HomeModelAdvice(SettingService settingService, LanguageService languageService) {
        this.settingService = settingService;
        this.languageService = languageService;
    }

    @ModelAttribute
    public void injectCommon(Map<String, Object> model, HttpSession session) {
        try {
            model.put("siteConfig", settingService.getAll());
            model.put("lang", languageService.commonLang());
        } catch (Exception ignored) {
        }
        // 前台用户登录态
        Object userId = session.getAttribute(SessionKeys.USER_ID);
        if (userId != null) {
            model.put("currentUserId", userId);
            model.put("currentUserName", session.getAttribute(SessionKeys.USER_NAME));
        }
    }
}
