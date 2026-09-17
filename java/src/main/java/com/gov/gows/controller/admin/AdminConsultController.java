package com.gov.gows.controller.admin;

import com.gov.gows.common.SessionKeys;
import com.gov.gows.entity.Admin;
import com.gov.gows.entity.Consult;
import com.gov.gows.service.ConsultService;
import com.gov.gows.service.LogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 后台咨询投诉管理控制器。
 */
@Controller
@RequestMapping("/admin/consult")
public class AdminConsultController {

    private final ConsultService consultService;
    private final LogService logService;

    public AdminConsultController(ConsultService consultService, LogService logService) {
        this.consultService = consultService;
        this.logService = logService;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(required = false) Integer status,
                       @RequestParam(defaultValue = "1") int page,
                       Model model) {
        var result = consultService.adminPage(status, page, 15);
        model.addAttribute("list", result.getList());
        model.addAttribute("page", result.getPage());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("totalPage", result.getTotalPage());
        model.addAttribute("status", status);
        return "admin/consult-list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Consult c = consultService.findById(id);
        if (c != null && c.getStatus() != null && c.getStatus() == 0) {
            consultService.markRead(id);
            c.setStatus(1);
        }
        model.addAttribute("consult", c);
        return "admin/consult-view";
    }

    @PostMapping("/reply")
    public String reply(@RequestParam Long id,
                        @RequestParam String reply,
                        HttpSession session,
                        RedirectAttributes ra) {
        Admin admin = (Admin) session.getAttribute(SessionKeys.ADMIN);
        Long adminId = admin == null ? 0L : admin.getId();
        consultService.reply(id, reply, adminId);
        logService.log(adminId, admin == null ? null : admin.getUsername(),
                "consult", "reply", "回复咨询 ID:" + id, null);
        ra.addFlashAttribute("flashMessage", "回复成功");
        return "redirect:/admin/consult";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        consultService.delete(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/consult";
    }
}
