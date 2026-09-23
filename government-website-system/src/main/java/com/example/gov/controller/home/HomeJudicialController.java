package com.example.gov.controller.home;

import com.example.gov.entity.Judicial;
import com.example.gov.service.JudicialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 前台裁判文书检索控制器。
 */
@Controller
public class HomeJudicialController {

    private final JudicialService judicialService;

    public HomeJudicialController(JudicialService judicialService) {
        this.judicialService = judicialService;
    }

    @GetMapping("/judicial")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "1") int page,
                       Model model) {
        if (keyword != null && !keyword.isBlank()) {
            var result = judicialService.search(keyword.trim(), page, 10);
            model.addAttribute("judicials", result.getList());
            model.addAttribute("total", result.getTotal());
            model.addAttribute("isSearch", true);
            model.addAttribute("keyword", keyword);
        } else {
            var result = judicialService.publicPage(page, 10);
            model.addAttribute("judicials", result.getList());
            model.addAttribute("total", result.getTotal());
            model.addAttribute("isSearch", false);
            model.addAttribute("keyword", "");
        }
        model.addAttribute("page", page);
        long total = (Long) model.getAttribute("total");
        model.addAttribute("totalPages", (int) Math.ceil((double) total / 10));
        model.addAttribute("title", "裁判文书");
        return "home/judicial";
    }

    @GetMapping("/judicial/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Judicial j = judicialService.findById(id);
        if (j == null || j.getStatus() == null || j.getStatus() != 1
            || j.getIsPublic() == null || j.getIsPublic() != 1) {
            model.addAttribute("errorMsg", "文书不存在或未公开");
            return "home/error";
        }
        judicialService.increaseViews(id);
        model.addAttribute("judicial", j);
        model.addAttribute("title", j.getCaseName());
        return "home/judicial-detail";
    }
}
