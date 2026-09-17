package com.gov.gows.controller.admin;

import com.gov.gows.common.BizException;
import com.gov.gows.common.SessionKeys;
import com.gov.gows.entity.Admin;
import com.gov.gows.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    /** 后台登录页。 */
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute(SessionKeys.ADMIN_ID) != null) {
            return "redirect:/admin";
        }
        return "admin/login";
    }

    /** 后台登录提交。 */
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

    /** 后台退出。 */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SessionKeys.ADMIN_ID);
        session.removeAttribute(SessionKeys.ADMIN);
        return "redirect:/admin/login";
    }

    /** 图形验证码（输出 PNG，验证码文本写入 session）。 */
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
