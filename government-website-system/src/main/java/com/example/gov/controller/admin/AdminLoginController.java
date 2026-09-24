package com.example.gov.controller.admin;

import com.example.gov.common.BizException;
import com.example.gov.common.SessionKeys;
import com.example.gov.entity.Admin;
import com.example.gov.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 后台登录控制器。
 */
@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    private final AdminService adminService;

    public AdminLoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * 后台登录页。
     *
     * <p>若当前会话已登录后台则直接进入后台；若仅登录了前台账号，
     * 在登录页给出提示，引导使用管理员账号登录，而不是报 403。</p>
     */
    @GetMapping("/login")
    public String loginPage(HttpSession session, org.springframework.ui.Model model) {
        if (session.getAttribute(SessionKeys.ADMIN_ID) != null) {
            return "redirect:/admin";
        }
        // 前台已登录但未登录后台时给出提示
        Object frontUserId = session.getAttribute(SessionKeys.USER_ID);
        if (frontUserId != null) {
            model.addAttribute("frontNotice",
                "当前前台账号（" + session.getAttribute(SessionKeys.USER_NAME) + "）已登录，后台请使用管理员账号登录");
        }
        return "admin/login";
    }

    /**
     * 后台登录提交。
     */
    @PostMapping("/login/doLogin")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpServletRequest request,
                          HttpSession session,
                          RedirectAttributes ra) {
        try {
            Admin admin = adminService.login(username, password, getClientIp(request),
                request.getHeader("User-Agent"));
            session.setAttribute(SessionKeys.ADMIN_ID, admin.getId());
            session.setAttribute(SessionKeys.ADMIN, admin);
            ra.addFlashAttribute("flashMessage", "登录成功");
            return "redirect:/admin";
        } catch (BizException e) {
            ra.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/admin/login";
        }
    }

    /**
     * 后台退出。
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SessionKeys.ADMIN_ID);
        session.removeAttribute(SessionKeys.ADMIN);
        return "redirect:/admin/login";
    }

    /**
     * 图形验证码（输出 PNG，验证码文本写入 session）。
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, OutputStream out) throws IOException {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random rnd = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        request.getSession().setAttribute("captcha", code.toString());

        int w = 120, h = 40;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, w, h);
        // 干扰线
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(100 + rnd.nextInt(100), 100 + rnd.nextInt(100), 100 + rnd.nextInt(100)));
            g.drawLine(rnd.nextInt(w), rnd.nextInt(h), rnd.nextInt(w), rnd.nextInt(h));
        }
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.setColor(new Color(50, 50, 50));
        for (int i = 0; i < code.length(); i++) {
            g.drawString(String.valueOf(code.charAt(i)), 15 + i * 25, 28 + rnd.nextInt(6));
        }
        g.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bos);
        out.write(bos.toByteArray());
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isBlank()) ? request.getRemoteAddr() : ip.split(",")[0].trim();
    }
}
