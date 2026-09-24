package com.example.gov.controller.admin;

import com.example.gov.common.BizException;
import com.example.gov.service.BackupService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 后台数据备份管理控制器（导出/下载/删除/恢复）。
 */
@Controller
@RequestMapping("/admin/backup")
@Slf4j
public class AdminBackupController {

    private final BackupService backupService;

    @org.springframework.beans.factory.annotation.Value("${gov.backup-path:./backup/}")
    private String backupPath;

    public AdminBackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("files", backupService.listFiles());
        return "admin/backup";
    }

    /**
     * 创建新备份。
     */
    @PostMapping("/create")
    public String create(RedirectAttributes ra) {
        log.info("[{}] AdminBackupController.create 调用", Thread.currentThread().getName());
        try {
            String name = backupService.exportDatabase();
            ra.addFlashAttribute("flashMessage", "备份创建成功: " + name);
        } catch (BizException e) {
            ra.addFlashAttribute("flashError", e.getMessage());
        }
        return "redirect:/admin/backup";
    }

    /**
     * 下载备份文件。
     */
    @GetMapping("/download")
    public void download(@RequestParam String filename, HttpServletResponse response) throws IOException {
        String safe = Paths.get(filename).getFileName().toString();
        Path file = Paths.get(backupPath).resolve(safe).normalize();
        if (!Files.exists(file)) {
            response.sendError(404, "备份文件不存在");
            return;
        }
        response.setContentType("application/sql");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + safe + "\"");
        Files.copy(file, response.getOutputStream());
    }

    /**
     * 删除备份文件。
     */
    @PostMapping("/delete")
    public String delete(@RequestParam String filename, RedirectAttributes ra) {
        log.info("[{}] AdminBackupController.delete 调用, 参数: filename={}", Thread.currentThread().getName(), filename);
        if (backupService.delete(filename)) {
            ra.addFlashAttribute("flashMessage", "删除成功");
        } else {
            ra.addFlashAttribute("flashError", "文件不存在");
        }
        return "redirect:/admin/backup";
    }

    /**
     * 上传 SQL 恢复数据库（危险操作）。
     */
    @PostMapping("/restore")
    public String restore(@RequestParam("file") MultipartFile file, RedirectAttributes ra) throws IOException {
        if (file.isEmpty()) {
            ra.addFlashAttribute("flashError", "请选择 SQL 备份文件");
            return "redirect:/admin/backup";
        }
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        try {
            int count = backupService.restore(content);
            ra.addFlashAttribute("flashMessage", "恢复完成，共执行 " + count + " 条 SQL 语句");
        } catch (Exception e) {
            ra.addFlashAttribute("flashError", "恢复失败: " + e.getMessage());
        }
        return "redirect:/admin/backup";
    }
}
