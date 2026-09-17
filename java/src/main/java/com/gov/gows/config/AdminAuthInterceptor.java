package com.gov.gows.config;

import com.gov.gows.common.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 后台登录拦截器。
 *
 * <p>对 /admin/** 除登录相关白名单外的请求校验后台登录态；
 * 未登录重定向到 /admin/login；普通前台用户访问后台则返回 403。</p>
 */
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        // 白名单：登录页、登录处理、静态资源
        if (uri.equals("/admin/login") || uri.equals("/admin/login/doLogin")
                || uri.equals("/admin/captcha") || uri.startsWith("/admin/assets")
                || uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images")
                || uri.startsWith("/uploads") || uri.startsWith("/media")) {
            return true;
        }

        HttpSession session = request.getSession();
        Object adminId = session.getAttribute(SessionKeys.ADMIN_ID);
        Object admin = session.getAttribute(SessionKeys.ADMIN);
        if (adminId == null || admin == null) {
            // 普通前台用户访问后台 -> 403，未登录 -> 跳转登录
            if (session.getAttribute(SessionKeys.USER_ID) != null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "您没有后台访问权限");
            } else {
                response.sendRedirect("/admin/login");
            }
            return false;
        }
        return true;
    }
}
