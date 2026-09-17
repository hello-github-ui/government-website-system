package com.gov.gows.controller.admin;

import com.gov.gows.common.SessionKeys;
import com.gov.gows.entity.Admin;
import com.gov.gows.service.ConsultService;
import com.gov.gows.service.JudicialService;
import com.gov.gows.service.NoticeService;
import com.gov.gows.service.PolicyService;
import com.gov.gows.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台仪表盘控制器。
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final NoticeService noticeService;
    private final PolicyService policyService;
    private final JudicialService judicialService;
    private final UserService userService;
    private final ConsultService consultService;

    public AdminDashboardController(NoticeService noticeService, PolicyService policyService,
                                    JudicialService judicialService, UserService userService,
                                    ConsultService consultService) {
        this.noticeService = noticeService;
        this.policyService = policyService;
        this.judicialService = judicialService;
        this.userService = userService;
        this.consultService = consultService;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String index(HttpSession session, Model model) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("noticeCount", noticeService.page(1, 1).getTotal());
        stats.put("policyCount", policyService.page(1, 1).getTotal());
        stats.put("judicialCount", judicialService.page(1, 1).getTotal());
        stats.put("userCount", userService.page(1, 1).getTotalElements());
        stats.put("consultPending", consultService.pendingCount());

        model.addAttribute("stats", stats);
        model.addAttribute("latestNotices", noticeService.latest(5));
        model.addAttribute("pendingConsults",
                consultService.adminPage(0, 1, 5).getList());
        model.addAttribute("admin", session.getAttribute(SessionKeys.ADMIN));
        return "admin/dashboard";
    }
}
