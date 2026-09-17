package com.gov.gows.controller.admin;

import com.gov.gows.entity.Judicial;
import com.gov.gows.service.JudicialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * 后台裁判文书管理控制器。
 */
@Controller
@RequestMapping("/admin/judicial")
public class AdminJudicialController {

    private final JudicialService judicialService;

    public AdminJudicialController(JudicialService judicialService) {
        this.judicialService = judicialService;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        var result = judicialService.page(page, 15);
        model.addAttribute("list", result.getList());
        model.addAttribute("page", result.getPage());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("totalPage", result.getTotalPage());
        return "admin/judicial-list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "admin/judicial-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam String caseNo,
                      @RequestParam String caseName,
                      @RequestParam String court,
                      @RequestParam(required = false) String caseType,
                      @RequestParam(required = false) String content,
                      @RequestParam(required = false) String judgeDate,
                      @RequestParam(defaultValue = "1") Integer checkStatus,
                      @RequestParam(defaultValue = "1") Integer isPublic,
                      @RequestParam(defaultValue = "1") Integer status,
                      RedirectAttributes ra) {
        Judicial j = new Judicial();
        j.setCaseNo(caseNo);
        j.setCaseName(caseName);
        j.setCourt(court);
        j.setCaseType(caseType);
        j.setContent(content);
        if (judgeDate != null && !judgeDate.isBlank()) {
            j.setJudgeDate(LocalDate.parse(judgeDate));
        }
        j.setCheckStatus(checkStatus);
        j.setIsPublic(isPublic);
        j.setStatus(status);
        judicialService.create(j);
        ra.addFlashAttribute("flashMessage", "添加成功");
        return "redirect:/admin/judicial";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("info", judicialService.findById(id));
        return "admin/judicial-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String caseNo,
                       @RequestParam String caseName,
                       @RequestParam String court,
                       @RequestParam(required = false) String caseType,
                       @RequestParam(required = false) String content,
                       @RequestParam(required = false) String judgeDate,
                       @RequestParam(defaultValue = "1") Integer checkStatus,
                       @RequestParam(defaultValue = "1") Integer isPublic,
                       @RequestParam(defaultValue = "1") Integer status,
                       RedirectAttributes ra) {
        Judicial j = judicialService.findById(id);
        j.setCaseNo(caseNo);
        j.setCaseName(caseName);
        j.setCourt(court);
        j.setCaseType(caseType);
        j.setContent(content);
        if (judgeDate != null && !judgeDate.isBlank()) {
            j.setJudgeDate(LocalDate.parse(judgeDate));
        }
        j.setCheckStatus(checkStatus);
        j.setIsPublic(isPublic);
        j.setStatus(status);
        judicialService.update(j);
        ra.addFlashAttribute("flashMessage", "更新成功");
        return "redirect:/admin/judicial";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        judicialService.delete(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/judicial";
    }
}
