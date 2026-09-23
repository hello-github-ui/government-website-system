package com.example.gov.controller.admin;

import com.example.gov.service.SettingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * 后台系统设置控制器。
 */
@Controller
@RequestMapping("/admin/settings")
public class AdminSettingsController {

    private final SettingService settingService;

    public AdminSettingsController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("config", settingService.getAll());
        return "admin/settings";
    }

    @PostMapping("/save")
    public String save(@RequestParam Map<String, String> form, RedirectAttributes ra) {
        // 仅保存已知设置键，避免写入任意字段
        String[] keys = {"site_title", "site_subtitle", "site_url", "site_logo", "site_favicon",
            "seo_keywords", "seo_description", "icp_number", "police_number", "copyright",
            "contact_address", "contact_phone", "contact_email", "contact_work_time"};
        for (String k : keys) {
            if (form.containsKey(k)) {
                settingService.set(k, form.get(k));
            }
        }
        ra.addFlashAttribute("flashMessage", "保存成功");
        return "redirect:/admin/settings";
    }

    @PostMapping("/clearCache")
    public String clearCache(RedirectAttributes ra) {
        settingService.clearCache();
        ra.addFlashAttribute("flashMessage", "缓存清除成功");
        return "redirect:/admin/settings";
    }

    @GetMapping("/system")
    public String system(Model model) {
        model.addAttribute("info", Map.of(
            "javaVersion", System.getProperty("java.version"),
            "osName", System.getProperty("os.name"),
            "serverTime", java.time.LocalDateTime.now().toString()
        ));
        return "admin/settings-system";
    }
}
