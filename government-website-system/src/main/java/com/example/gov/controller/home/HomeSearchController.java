package com.example.gov.controller.home;

import com.example.gov.service.JudicialService;
import com.example.gov.service.NoticeService;
import com.example.gov.service.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 全站搜索控制器。
 */
@Controller
@Slf4j
public class HomeSearchController {

    private final NoticeService noticeService;
    private final PolicyService policyService;
    private final JudicialService judicialService;

    public HomeSearchController(NoticeService noticeService, PolicyService policyService,
                                JudicialService judicialService) {
        this.noticeService = noticeService;
        this.policyService = policyService;
        this.judicialService = judicialService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q,
                         @RequestParam(defaultValue = "all") String type,
                         @RequestParam(defaultValue = "1") int page,
                         Model model) {
        String kw = (q == null) ? "" : q.trim();
        model.addAttribute("keyword", kw);
        model.addAttribute("type", type);
        model.addAttribute("title", "搜索");
        if (kw.isEmpty()) {
            return "home/search";
        }
        switch (type) {
            case "notice" -> model.addAttribute("noticeResult", noticeService.search(kw, page, 10));
            case "policy" -> model.addAttribute("policyResult", policyService.search(kw, page, 10));
            case "judicial" -> model.addAttribute("judicialResult", judicialService.search(kw, page, 10));
            default -> {
                // 聚合三类前 5 条
                model.addAttribute("noticeResult", noticeService.search(kw, 1, 5));
                model.addAttribute("policyResult", policyService.search(kw, 1, 5));
                model.addAttribute("judicialResult", judicialService.search(kw, 1, 5));
            }
        }
        return "home/search";
    }
}
