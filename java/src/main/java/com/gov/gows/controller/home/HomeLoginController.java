package com.gov.gows.controller.home;

import com.gov.gows.common.BizException;
import com.gov.gows.common.SessionKeys;
import com.gov.gows.entity.User;
import com.gov.gows.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 前台用户登录/注册控制器。
 */
@Controller
public class HomeLoginController {

    private final UserService userService;

    public HomeLoginController(UserService userService) {
        this.userService = userService;
    }

    /** 登录页。 */
    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute(SessionKeys.USER_ID) != null) {
            return "redirect:/user/profile";
        }
        return "home/login";
    }

    /** 登录提交。 */
    @PostMapping("/login/doLogin")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpServletRequest request,
                          HttpSession session,
                          RedirectAttributes ra) {
        try {
            User user = userService.login(username, password, getClientIp(request));
            session.setAttribute(SessionKeys.USER_ID, user.getId());
            session.setAttribute(SessionKeys.USER_NAME, user.getUsername());
            ra.addFlashAttribute("flashMessage", "登录成功");
            return "redirect:/user/profile";
        } catch (BizException e) {
            ra.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/login";
        }
    }

    /** 注册页。 */
    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute(SessionKeys.USER_ID) != null) {
            return "redirect:/user/profile";
        }
        return "home/register";
    }

    /** 注册提交。 */
    @PostMapping("/register/doRegister")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String confirmPassword,
                             @RequestParam(required = false) String realName,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false) String email,
                             RedirectAttributes ra) {
        try {
            userService.register(username, password, confirmPassword, realName, phone, email);
            ra.addFlashAttribute("flashMessage", "注册成功，请登录");
            return "redirect:/login";
        } catch (BizException e) {
            ra.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/register";
        }
    }

    /** 前台退出。 */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SessionKeys.USER_ID);
        session.removeAttribute(SessionKeys.USER_NAME);
        return "redirect:/";
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isBlank()) ? request.getRemoteAddr() : ip.split(",")[0].trim();
    }
}
