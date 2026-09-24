package com.example.gov.controller.home;

import com.example.gov.common.SessionKeys;
import com.example.gov.entity.Consult;
import com.example.gov.service.ConsultService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

/**
 * 前台咨询投诉控制器。
 */
@Controller
@Slf4j
public class HomeConsultController {

    private final ConsultService consultService;

    public HomeConsultController(ConsultService consultService) {
        this.consultService = consultService;
    }

    /**
     * 公开的已回复咨询列表。
     */
    @GetMapping("/consult")
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        var result = consultService.publicReplied(page, 10);
        model.addAttribute("consults", result.getList());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("page", result.getPage());
        model.addAttribute("totalPages", result.getTotalPage());
        model.addAttribute("title", "咨询投诉");
        return "home/consult";
    }

    /**
     * 提交咨询页（需登录）。
     */
    @GetMapping("/consult/submit")
    public String submitPage(HttpSession session) {
        log.info("[{}] HomeConsultController.submitPage 调用", Thread.currentThread().getName());
        if (session.getAttribute(SessionKeys.USER_ID) == null) {
            return "redirect:/login";
        }
        return "home/consult-submit";
    }

    /**
     * 保存咨询。
     */
    @PostMapping("/consult/save")
    public String save(@RequestParam String title,
                       @RequestParam String content,
                       @RequestParam(defaultValue = "1") Integer type,
                       @RequestParam(value = "is_public", defaultValue = "1") Integer isPublic,
                       HttpSession session,
                       RedirectAttributes ra) {
        Long userId = (Long) session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) {
            ra.addFlashAttribute("flashError", "请先登录");
            return "redirect:/login";
        }
        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            ra.addFlashAttribute("flashError", "标题和内容不能为空");
            return "redirect:/consult/submit";
        }
        if (title.length() > 200) {
            ra.addFlashAttribute("flashError", "标题长度不能超过200字");
            return "redirect:/consult/submit";
        }
        Consult c = new Consult();
        c.setUserId(userId);
        c.setTitle(title);
        c.setContent(content);
        c.setType(type);
        c.setIsPublic(isPublic);
        c.setStatus(0);
        consultService.create(c);
        ra.addFlashAttribute("flashMessage", "提交成功，我们会尽快处理您的咨询");
        return "redirect:/consult/my";
    }

    /**
     * 我的咨询。
     */
    @GetMapping("/consult/my")
    public String my(HttpSession session,
                     @RequestParam(defaultValue = "1") int page,
                     Model model) {
        Long userId = (Long) session.getAttribute(SessionKeys.USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }
        var result = consultService.myList(userId, page, 10);
        model.addAttribute("consults", result.getList());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("page", result.getPage());
        model.addAttribute("totalPages", result.getTotalPage());
        model.addAttribute("title", "我的咨询");
        return "home/consult-my";
    }

    /**
     * 咨询详情（公开或本人可见）。
     */
    @GetMapping("/consult/detail/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        Consult c = consultService.findById(id);
        if (c == null) {
            model.addAttribute("errorMsg", "咨询不存在");
            return "home/error";
        }
        Long userId = (Long) session.getAttribute(SessionKeys.USER_ID);
        boolean self = userId != null && userId.equals(c.getUserId());
        boolean publicVisible = c.getIsPublic() != null && c.getIsPublic() == 1;
        if (!publicVisible && !self) {
            model.addAttribute("errorMsg", "您没有权限查看此咨询");
            return "home/error";
        }
        model.addAttribute("consult", c);
        model.addAttribute("title", c.getTitle());
        return "home/consult-detail";
    }
}
