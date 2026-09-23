package com.example.gov.controller.home;

import com.example.gov.entity.Notice;
import com.example.gov.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 前台公告控制器。
 */
@Controller
public class HomeNoticeController {

    private final NoticeService noticeService;

    public HomeNoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/notice")
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        var result = noticeService.publishedPage(page, 10);
        model.addAttribute("notices", result.getList());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("page", result.getPage());
        model.addAttribute("totalPages", result.getTotalPage());
        model.addAttribute("title", "政务公告");
        return "home/notice";
    }

    @GetMapping("/notice/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Notice notice = noticeService.findById(id);
        if (notice == null || notice.getStatus() == null || notice.getStatus() != 1) {
            model.addAttribute("errorMsg", "公告不存在或已下架");
            return "home/error";
        }
        noticeService.increaseViews(id);
        model.addAttribute("notice", notice);
        model.addAttribute("title", notice.getTitle());
        return "home/notice-detail";
    }
}
