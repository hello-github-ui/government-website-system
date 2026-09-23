package com.example.gov.controller.home;

import com.example.gov.common.SessionKeys;
import com.example.gov.entity.User;
import com.example.gov.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 前台用户中心控制器。
 */
@Controller
@RequestMapping("/user")
public class HomeUserController {

    private final UserService userService;

    public HomeUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 个人中心。
     */
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        model.addAttribute("title", "个人中心");
        return "home/user-profile";
    }

    /**
     * 修改密码页 / 提交。
     */
    @GetMapping("/password")
    public String passwordPage(HttpSession session) {
        if (session.getAttribute(SessionKeys.USER_ID) == null) {
            return "redirect:/login";
        }
        return "home/user-password";
    }

    @PostMapping("/password")
    public String changePassword(@RequestParam("old_password") String oldPwd,
                                 @RequestParam("new_password") String newPwd,
                                 @RequestParam("confirm_password") String confirmPwd,
                                 HttpSession session,
                                 RedirectAttributes ra) {
        Long userId = (Long) session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            userService.changePassword(userId, oldPwd, newPwd, confirmPwd);
            ra.addFlashAttribute("flashMessage", "密码修改成功");
            return "redirect:/user/profile";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/user/password";
        }
    }
}
