package com.gov.gows.controller.admin;

import com.gov.gows.entity.Language;
import com.gov.gows.mapper.LanguageMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 后台文字配置控制器。
 */
@Controller
@RequestMapping("/admin/language")
public class AdminLanguageController {

    private final LanguageMapper languageMapper;

    public AdminLanguageController(LanguageMapper languageMapper) {
        this.languageMapper = languageMapper;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "common") String group, Model model) {
        model.addAttribute("configs", languageMapper.selectByGroup(group, "common"));
        model.addAttribute("group", group);
        model.addAttribute("groups", java.util.Map.of(
                "common", "通用", "nav", "导航", "user", "用户相关", "judicial", "裁判文书"));
        return "admin/language-list";
    }

    @PostMapping("/save")
    public String save(@RequestParam java.util.Map<String, String> configs, RedirectAttributes ra) {
        configs.forEach((k, v) -> {
            if (k.startsWith("cfg_")) {
                String key = k.substring(4);
                languageMapper.updateValueByKey(key, v);
            }
        });
        ra.addFlashAttribute("flashMessage", "保存成功");
        return "redirect:/admin/language";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        languageMapper.deleteById(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/language";
    }
}
