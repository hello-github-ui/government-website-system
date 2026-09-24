package com.example.gov.controller.admin;

import com.example.gov.mapper.LanguageMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

/**
 * 后台文字配置控制器。
 */
@Controller
@RequestMapping("/admin/language")
@Slf4j
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
        log.info("[{}] AdminLanguageController.save 调用", Thread.currentThread().getName());
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
        log.info("[{}] AdminLanguageController.delete 调用, 参数: id={}", Thread.currentThread().getName(), id);
        languageMapper.deleteById(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/language";
    }
}
