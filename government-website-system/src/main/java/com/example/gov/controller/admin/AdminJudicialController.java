package com.example.gov.controller.admin;

import com.example.gov.common.BizException;
import com.example.gov.entity.Judicial;
import com.example.gov.service.FileStorageService;
import com.example.gov.service.JudicialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;

/**
 * 后台裁判文书管理控制器（含附件上传）。
 */
@Controller
@RequestMapping("/admin/judicial")
@Slf4j
public class AdminJudicialController {

    private final JudicialService judicialService;
    private final FileStorageService fileStorageService;

    public AdminJudicialController(JudicialService judicialService, FileStorageService fileStorageService) {
        this.judicialService = judicialService;
        this.fileStorageService = fileStorageService;
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
        log.info("[{}] AdminJudicialController.addPage 调用", Thread.currentThread().getName());
        return "admin/judicial-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam(value = "case_no") String caseNo,
                      @RequestParam(value = "case_name") String caseName,
                      @RequestParam String court,
                      @RequestParam(value = "case_type", required = false) String caseType,
                      @RequestParam(required = false) String content,
                      @RequestParam(value = "judge_date", required = false) String judgeDate,
                      @RequestParam(value = "check_status", defaultValue = "1") Integer checkStatus,
                      @RequestParam(value = "is_public", defaultValue = "1") Integer isPublic,
                      @RequestParam(defaultValue = "1") Integer status,
                      @RequestParam(value = "attachment", required = false) MultipartFile attachment,
                      RedirectAttributes ra) throws IOException {
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
        if (attachment != null && !attachment.isEmpty()) {
            try {
                j.setAttachment(fileStorageService.store(attachment, 10));
            } catch (BizException e) {
                ra.addFlashAttribute("flashError", e.getMessage());
                return "redirect:/admin/judicial/add";
            }
        }
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
                       @RequestParam(value = "case_no") String caseNo,
                       @RequestParam(value = "case_name") String caseName,
                       @RequestParam String court,
                       @RequestParam(value = "case_type", required = false) String caseType,
                       @RequestParam(required = false) String content,
                       @RequestParam(value = "judge_date", required = false) String judgeDate,
                       @RequestParam(value = "check_status", defaultValue = "1") Integer checkStatus,
                       @RequestParam(value = "is_public", defaultValue = "1") Integer isPublic,
                       @RequestParam(defaultValue = "1") Integer status,
                       @RequestParam(value = "attachment", required = false) MultipartFile attachment,
                       RedirectAttributes ra) throws IOException {
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
        if (attachment != null && !attachment.isEmpty()) {
            try {
                j.setAttachment(fileStorageService.store(attachment, 10));
            } catch (BizException e) {
                ra.addFlashAttribute("flashError", e.getMessage());
                return "redirect:/admin/judicial/edit/" + id;
            }
        }
        judicialService.update(j);
        ra.addFlashAttribute("flashMessage", "更新成功");
        return "redirect:/admin/judicial";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        log.info("[{}] AdminJudicialController.delete 调用, 参数: id={}", Thread.currentThread().getName(), id);
        judicialService.delete(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/judicial";
    }
}
