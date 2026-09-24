package com.example.gov.controller.admin;

import com.example.gov.common.BizException;
import com.example.gov.entity.Policy;
import com.example.gov.service.FileStorageService;
import com.example.gov.service.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;

/**
 * 后台政策法规管理控制器（含附件上传）。
 */
@Controller
@RequestMapping("/admin/policy")
public class AdminPolicyController {

    private final PolicyService policyService;
    private final FileStorageService fileStorageService;

    public AdminPolicyController(PolicyService policyService, FileStorageService fileStorageService) {
        this.policyService = policyService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        var result = policyService.page(page, 15);
        model.addAttribute("list", result.getList());
        model.addAttribute("page", result.getPage());
        model.addAttribute("total", result.getTotal());
        model.addAttribute("totalPage", result.getTotalPage());
        return "admin/policy-list";
    }

    @GetMapping("/add")
    public String addPage() {
        return "admin/policy-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title,
                      @RequestParam(value = "publish_org", required = false) String publishOrg,
                      @RequestParam(required = false) String content,
                      @RequestParam(value = "publish_date", required = false) String publishDate,
                      @RequestParam(defaultValue = "1") Integer status,
                      @RequestParam(value = "attachment", required = false) MultipartFile attachment,
                      RedirectAttributes ra) throws IOException {
        Policy p = new Policy();
        p.setTitle(title);
        p.setPublishOrg(publishOrg);
        p.setContent(content);
        p.setPublishDate(publishDate == null || publishDate.isBlank() ? LocalDate.now() : LocalDate.parse(publishDate));
        p.setStatus(status);
        if (attachment != null && !attachment.isEmpty()) {
            try {
                p.setAttachment(fileStorageService.store(attachment, 10));
            } catch (BizException e) {
                ra.addFlashAttribute("flashError", e.getMessage());
                return "redirect:/admin/policy/add";
            }
        }
        policyService.create(p);
        ra.addFlashAttribute("flashMessage", "添加成功");
        return "redirect:/admin/policy";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("info", policyService.findById(id));
        return "admin/policy-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String title,
                       @RequestParam(value = "publish_org", required = false) String publishOrg,
                       @RequestParam(required = false) String content,
                       @RequestParam(value = "publish_date", required = false) String publishDate,
                       @RequestParam(defaultValue = "1") Integer status,
                       @RequestParam(value = "attachment", required = false) MultipartFile attachment,
                       RedirectAttributes ra) throws IOException {
        Policy p = policyService.findById(id);
        p.setTitle(title);
        p.setPublishOrg(publishOrg);
        p.setContent(content);
        if (publishDate != null && !publishDate.isBlank()) {
            p.setPublishDate(LocalDate.parse(publishDate));
        }
        p.setStatus(status);
        if (attachment != null && !attachment.isEmpty()) {
            try {
                p.setAttachment(fileStorageService.store(attachment, 10));
            } catch (BizException e) {
                ra.addFlashAttribute("flashError", e.getMessage());
                return "redirect:/admin/policy/edit/" + id;
            }
        }
        policyService.update(p);
        ra.addFlashAttribute("flashMessage", "更新成功");
        return "redirect:/admin/policy";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes ra) {
        policyService.delete(id);
        ra.addFlashAttribute("flashMessage", "删除成功");
        return "redirect:/admin/policy";
    }
}
