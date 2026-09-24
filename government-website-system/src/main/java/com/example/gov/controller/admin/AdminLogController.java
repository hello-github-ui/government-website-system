package com.example.gov.controller.admin;

import com.example.gov.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

/**
 * 后台操作日志控制器。
 */
@Controller
@RequestMapping("/admin/log")
@Slf4j
public class AdminLogController {

    private final LogService logService;

    public AdminLogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        var result = logService.page(page, 20);
        model.addAttribute("logs", result.getList());
        model.addAttribute("page", result.getPage());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("totalPage", result.getTotalPage());
        return "admin/log-list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("log", logService.findById(id));
        return "admin/log-view";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        log.info("[{}] AdminLogController.delete 调用, 参数: id={}", Thread.currentThread().getName(), id);
        logService.delete(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/log";
    }

    @PostMapping("/clear")
    public String clear(@RequestParam(defaultValue = "30") int days, RedirectAttributes ra) {
        logService.clearOlderThan(days);
        ra.addFlashAttribute("flashMessage", "已清空 " + days + " 天前的日志");
        return "redirect:/admin/log";
    }
}
