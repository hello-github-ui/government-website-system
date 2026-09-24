package com.example.gov.controller.admin;

import com.example.gov.common.SessionKeys;
import com.example.gov.entity.Admin;
import com.example.gov.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 后台管理员账号管理控制器。
 */
@Controller
@RequestMapping("/admin/admin")
@Slf4j
public class AdminAdminController {

    private final AdminService adminService;

    public AdminAdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        List<Admin> list = adminService.page(page, 15);
        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("total", adminService.count());
        model.addAttribute("totalPage", (int) Math.ceil((double) adminService.count() / 15));
        return "admin/admin-list";
    }

    @GetMapping("/add")
    public String addPage() {
        log.info("[{}] AdminAdminController.addPage 调用", Thread.currentThread().getName());
        return "admin/admin-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam String username,
                      @RequestParam String password,
                      @RequestParam String name,
                      @RequestParam(required = false) String email,
                      @RequestParam(value = "role_id", defaultValue = "1") Long roleId,
                      @RequestParam(defaultValue = "0") Integer isSuper,
                      @RequestParam(defaultValue = "1") Integer status,
                      RedirectAttributes ra) {
        if (adminService.findByUsername(username) != null) {
            ra.addFlashAttribute("flashError", "用户名已存在");
            return "redirect:/admin/admin/add";
        }
        Admin a = new Admin();
        a.setUsername(username);
        a.setName(name);
        a.setEmail(email);
        a.setRoleId(roleId);
        a.setIsSuper(isSuper);
        a.setIsAdmin(1);
        a.setAuthStatus(1);
        a.setStatus(status);
        adminService.createAdmin(a, password);
        ra.addFlashAttribute("flashMessage", "添加成功");
        return "redirect:/admin/admin";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("info", adminService.findById(id));
        return "admin/admin-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String name,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String phone,
                       @RequestParam(required = false) String password,
                       @RequestParam(value = "role_id", defaultValue = "1") Long roleId,
                       @RequestParam(defaultValue = "0") Integer isSuper,
                       @RequestParam(defaultValue = "1") Integer status,
                       RedirectAttributes ra) {
        Admin a = adminService.findById(id);
        a.setName(name);
        a.setEmail(email);
        a.setPhone(phone);
        a.setRoleId(roleId);
        a.setIsSuper(isSuper);
        a.setStatus(status);
        adminService.updateAdmin(a, password);
        ra.addFlashAttribute("flashMessage", "更新成功");
        return "redirect:/admin/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, HttpSession session, RedirectAttributes ra) {
        log.info("[{}] AdminAdminController.delete 调用, 参数: id={}", Thread.currentThread().getName(), id);
        Long currentId = (Long) session.getAttribute(SessionKeys.ADMIN_ID);
        if (id.equals(currentId)) {
            ra.addFlashAttribute("flashError", "不能删除当前登录账号");
            return "redirect:/admin/admin";
        }
        adminService.deleteAdmin(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/admin";
    }
}
