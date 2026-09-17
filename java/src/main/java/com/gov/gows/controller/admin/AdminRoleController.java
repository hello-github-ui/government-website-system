package com.gov.gows.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gov.gows.entity.AdminRole;
import com.gov.gows.mapper.AdminRoleMapper;
import com.gov.gows.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 后台角色管理控制器。
 */
@Controller
@RequestMapping("/admin/role")
public class AdminRoleController {

    private final AdminRoleMapper roleMapper;
    private final LogService logService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AdminRoleController(AdminRoleMapper roleMapper, LogService logService) {
        this.roleMapper = roleMapper;
        this.logService = logService;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        List<AdminRole> roles = roleMapper.selectAll();
        model.addAttribute("roles", roles);
        model.addAttribute("total", roles.size());
        return "admin/role-list";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("permissions", permissionTree());
        model.addAttribute("rolePermissions", List.of());
        return "admin/role-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam String roleName,
                      @RequestParam(required = false) String roleDesc,
                      @RequestParam(required = false) List<String> permissions,
                      @RequestParam(defaultValue = "1") Integer status,
                      RedirectAttributes ra) throws Exception {
        if (roleName == null || roleName.isBlank()) {
            ra.addFlashAttribute("flashError", "角色名称不能为空");
            return "redirect:/admin/role/add";
        }
        AdminRole r = new AdminRole();
        r.setRoleName(roleName);
        r.setRoleDesc(roleDesc);
        r.setPermissions(objectMapper.writeValueAsString(permissions == null ? List.of() : permissions));
        r.setStatus(status);
        roleMapper.insert(r);
        ra.addFlashAttribute("flashMessage", "添加成功");
        return "redirect:/admin/role";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) throws Exception {
        AdminRole role = roleMapper.findById(id);
        model.addAttribute("role", role);
        model.addAttribute("permissions", permissionTree());
        if (role != null && role.getPermissions() != null) {
            List<String> rp = objectMapper.readValue(role.getPermissions(), List.class);
            model.addAttribute("rolePermissions", rp);
        } else {
            model.addAttribute("rolePermissions", List.of());
        }
        return "admin/role-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String roleName,
                       @RequestParam(required = false) String roleDesc,
                       @RequestParam(required = false) List<String> permissions,
                       @RequestParam(defaultValue = "1") Integer status,
                       RedirectAttributes ra) throws Exception {
        AdminRole r = roleMapper.findById(id);
        r.setRoleName(roleName);
        r.setRoleDesc(roleDesc);
        r.setPermissions(objectMapper.writeValueAsString(permissions == null ? List.of() : permissions));
        r.setStatus(status);
        roleMapper.update(r);
        ra.addFlashAttribute("flashMessage", "更新成功");
        return "redirect:/admin/role";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        if (roleMapper.countAdminsByRole(id) > 0) {
            ra.addFlashAttribute("flashError", "该角色下有管理员，无法删除");
            return "redirect:/admin/role";
        }
        roleMapper.deleteById(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/role";
    }

    /** 权限点树（与 PHP 版一致）。 */
    private java.util.Map<String, Object> permissionTree() {
        java.util.Map<String, Object> m = new java.util.LinkedHashMap<>();
        m.put("dashboard", java.util.Map.of("name", "控制台", "permissions",
                java.util.Map.of("dashboard.view", "查看控制台")));
        m.put("content", java.util.Map.of("name", "内容管理", "permissions",
                java.util.Map.of("notice.manage", "公告管理", "policy.manage", "政策法规管理", "judicial.manage", "裁判文书管理")));
        m.put("user", java.util.Map.of("name", "用户管理", "permissions",
                java.util.Map.of("user.manage", "用户管理", "consult.manage", "咨询管理")));
        m.put("system", java.util.Map.of("name", "系统管理", "permissions",
                java.util.Map.of("admin.manage", "管理员管理", "role.manage", "角色管理",
                        "settings.manage", "系统设置", "log.view", "查看日志")));
        return m;
    }
}
