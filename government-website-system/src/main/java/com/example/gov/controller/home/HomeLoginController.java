package com.example.gov.controller.home;

import com.example.gov.common.BizException;
import com.example.gov.common.SessionKeys;
import com.example.gov.entity.User;
import com.example.gov.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 前台用户登录/注册控制器。
 */
@Slf4j
@Controller
public class HomeLoginController {

    private final UserService userService;

    public HomeLoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 前台登录页。
     */
    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute(SessionKeys.USER_ID) != null) {
            log.info("[{}] 前台已登录用户重复访问登录页, 跳转个人中心",
                Thread.currentThread().getName());
            return "redirect:/user/profile";
        }
        log.debug("[{}] 打开前台登录页", Thread.currentThread().getName());
        return "home/login";
    }

    /**
     * 前台登录提交。
     */
    @PostMapping("/login/doLogin")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpServletRequest request,
                          HttpSession session,
                          RedirectAttributes ra) {
        String thread = Thread.currentThread().getName();
        String ip = getClientIp(request);
        log.info("[{}] 前台登录提交 username={}, ip={}", thread, username, ip);
        try {
            User user = userService.login(username, password, ip);
            session.setAttribute(SessionKeys.USER_ID, user.getId());
            session.setAttribute(SessionKeys.USER_NAME, user.getUsername());
            ra.addFlashAttribute("flashMessage", "登录成功");
            log.info("[{}] 前台登录成功写入会话 userId={}, username={}", thread, user.getId(), username);
            return "redirect:/user/profile";
        } catch (BizException e) {
            ra.addFlashAttribute("flashError", e.getMessage());
            log.warn("[{}] 前台登录被拒绝 username={}, 原因={}", thread, username, e.getMessage());
            return "redirect:/login";
        }
    }

    /**
     * 前台注册页。
     */
    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute(SessionKeys.USER_ID) != null) {
            log.info("[{}] 前台用户已登录, 注册页跳转到个人中心",
                Thread.currentThread().getName());
            return "redirect:/user/profile";
        }
        log.debug("[{}] 打开前台注册页", Thread.currentThread().getName());
        return "home/register";
    }

    /**
     * 前台注册提交。
     */
    @PostMapping("/register/doRegister")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String confirmPassword,
                             @RequestParam(required = false) String realName,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false) String email,
                             RedirectAttributes ra) {
        String thread = Thread.currentThread().getName();
        log.info("[{}] 前台注册提交 username={}", thread, username);
        try {
            userService.register(username, password, confirmPassword, realName, phone, email);
            ra.addFlashAttribute("flashMessage", "注册成功，请登录");
            log.info("[{}] 前台注册成功 username={}", thread, username);
            return "redirect:/login";
        } catch (BizException e) {
            ra.addFlashAttribute("flashError", e.getMessage());
            log.warn("[{}] 前台注册被拒绝 username={}, 原因={}", thread, username, e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * 前台退出登录。
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        Object userId = session.getAttribute(SessionKeys.USER_ID);
        Object userName = session.getAttribute(SessionKeys.USER_NAME);
        log.info("[{}] 前台退出 userId={}, username={}, ip={}",
            Thread.currentThread().getName(), userId, userName, getClientIp(request));
        session.removeAttribute(SessionKeys.USER_ID);
        session.removeAttribute(SessionKeys.USER_NAME);
        return "redirect:/";
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isBlank()) ? request.getRemoteAddr() : ip.split(",")[0].trim();
    }
}
