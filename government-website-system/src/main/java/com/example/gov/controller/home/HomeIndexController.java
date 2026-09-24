package com.example.gov.controller.home;

import com.example.gov.service.NoticeService;
import com.example.gov.service.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * 前台首页控制器。
 */
@Controller
@Slf4j
public class HomeIndexController {

    private final NoticeService noticeService;
    private final PolicyService policyService;

    public HomeIndexController(NoticeService noticeService, PolicyService policyService) {
        this.noticeService = noticeService;
        this.policyService = policyService;
    }

    /**
     * 首页：轮播 + 快捷入口 + 最新公告/政策 + 文书检索入口。
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("notices", noticeService.latest(5));
        model.addAttribute("policies", policyService.latest(5));
        return "home/index";
    }
}
