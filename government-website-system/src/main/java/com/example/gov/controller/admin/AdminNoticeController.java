package com.example.gov.controller.admin;

import com.example.gov.entity.Notice;
import com.example.gov.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 后台公告管理控制器。
 */
@Controller
@RequestMapping("/admin/notice")
public class AdminNoticeController {

    private final NoticeService noticeService;

    public AdminNoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        var result = noticeService.page(page, 15);
        model.addAttribute("list", result.getList());
        model.addAttribute("page", result.getPage());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("totalPage", result.getTotalPage());
        return "admin/notice-list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "admin/notice-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title,
                      @RequestParam(required = false) String summary,
                      @RequestParam(required = false) String content,
                      @RequestParam(defaultValue = "0") Integer isTop,
                      @RequestParam(defaultValue = "0") Integer isImportant,
                      @RequestParam(defaultValue = "1") Integer status,
                      RedirectAttributes ra) {
        Notice n = new Notice();
        n.setTitle(title);
        n.setSummary(summary);
        n.setContent(content);
        n.setIsTop(isTop);
        n.setIsImportant(isImportant);
        n.setStatus(status);
        noticeService.create(n);
        ra.addFlashAttribute("flashMessage", "添加成功");
        return "redirect:/admin/notice";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("info", noticeService.findById(id));
        return "admin/notice-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String title,
                       @RequestParam(required = false) String summary,
                       @RequestParam(required = false) String content,
                       @RequestParam(defaultValue = "0") Integer isTop,
                       @RequestParam(defaultValue = "0") Integer isImportant,
                       @RequestParam(defaultValue = "1") Integer status,
                       RedirectAttributes ra) {
        Notice n = noticeService.findById(id);
        n.setTitle(title);
        n.setSummary(summary);
        n.setContent(content);
        n.setIsTop(isTop);
        n.setIsImportant(isImportant);
        n.setStatus(status);
        noticeService.update(n);
        ra.addFlashAttribute("flashMessage", "更新成功");
        return "redirect:/admin/notice";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        noticeService.delete(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/notice";
    }
}
