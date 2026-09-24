package com.example.gov.controller.admin;

import com.example.gov.entity.User;
import com.example.gov.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

/**
 * 后台前台用户管理控制器。
 */
@Controller
@RequestMapping("/admin/user")
@Slf4j
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        var result = userService.page(page, 15);
        model.addAttribute("list", result.getContent());
        model.addAttribute("page", result.getNumber() + 1);
        model.addAttribute("total", result.getTotalElements());
        model.addAttribute("totalPage", result.getTotalPages());
        return "admin/user-list";
    }

    @GetMapping("/add")
    public String addPage() {
        log.info("[{}] AdminUserController.addPage 调用", Thread.currentThread().getName());
        return "admin/user-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam String username,
                      @RequestParam String password,
                      @RequestParam(value = "real_name", required = false) String realName,
                      @RequestParam(required = false) String email,
                      @RequestParam(required = false) String phone,
                      @RequestParam(defaultValue = "1") Integer status,
                      RedirectAttributes ra) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(password); // service.register 会加密，但这里直接走 insert 需加密
        // 复用注册逻辑（含查重与加密）
        try {
            userService.register(username, password, password, realName, phone, email);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("flashError", e.getMessage());
            return "redirect:/admin/user/add";
        }
        ra.addFlashAttribute("flashMessage", "添加成功");
        return "redirect:/admin/user";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("info", userService.findById(id));
        return "admin/user-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam(value = "real_name", required = false) String realName,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String phone,
                       @RequestParam(required = false) String password,
                       @RequestParam(defaultValue = "1") Integer status,
                       RedirectAttributes ra) {
        User u = userService.findById(id);
        u.setRealName(realName);
        u.setEmail(email);
        u.setPhone(phone);
        u.setStatus(status);
        userService.update(u, password);
        ra.addFlashAttribute("flashMessage", "更新成功");
        return "redirect:/admin/user";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        log.info("[{}] AdminUserController.delete 调用, 参数: id={}", Thread.currentThread().getName(), id);
        userService.delete(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/user";
    }
}
