package com.gov.gows.controller.home;

import com.gov.gows.entity.Policy;
import com.gov.gows.service.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 前台政策法规控制器。
 */
@Controller
public class HomePolicyController {

    private final PolicyService policyService;

    public HomePolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/policy")
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        var result = policyService.publishedPage(page, 10);
        model.addAttribute("policies", result.getList());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("page", result.getPage());
        model.addAttribute("totalPages", result.getTotalPage());
        model.addAttribute("title", "政策法规");
        return "home/policy";
    }

    @GetMapping("/policy/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Policy policy = policyService.findById(id);
        if (policy == null || policy.getStatus() == null || policy.getStatus() != 1) {
            model.addAttribute("errorMsg", "政策不存在或已下架");
            return "home/error";
        }
        model.addAttribute("policy", policy);
        model.addAttribute("title", policy.getTitle());
        return "home/policy-detail";
    }
}
