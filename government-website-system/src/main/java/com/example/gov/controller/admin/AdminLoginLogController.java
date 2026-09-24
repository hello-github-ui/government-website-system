package com.example.gov.controller.admin;

import com.example.gov.entity.LoginLog;
import com.example.gov.mapper.LoginLogMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 后台登录日志查看控制器。
 */
@Controller
@RequestMapping("/admin/log/login")
public class AdminLoginLogController {

    private final LoginLogMapper loginLogMapper;

    public AdminLoginLogController(LoginLogMapper loginLogMapper) {
        this.loginLogMapper = loginLogMapper;
    }

    /**
     * 登录日志列表：支持按登录状态筛选与分页。
     */
    @GetMapping({"", "/"})
    public String list(@RequestParam(required = false) Integer loginStatus,
                       @RequestParam(defaultValue = "1") int page,
                       Model model) {
        final int size = 20;
        long total = loginLogMapper.count(loginStatus);
        int totalPage = Math.max(1, (int) Math.ceil((double) total / size));
        List<LoginLog> list = total == 0 ? List.of()
            : loginLogMapper.selectPage(loginStatus, (page - 1) * size, size);
        model.addAttribute("list", list);
        model.addAttribute("loginStatus", loginStatus == null ? -1 : loginStatus);
        model.addAttribute("page", page);
        model.addAttribute("total", total);
        model.addAttribute("totalPage", totalPage);
        return "admin/login-log-list";
    }
}
