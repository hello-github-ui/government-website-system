package com.gov.gows.controller.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 后台媒体库控制器（文件上传/列表/删除）。
 */
@Controller
@RequestMapping("/admin/media")
public class AdminMediaController {

    @Value("${gov.upload-path:./uploads/}")
    private String uploadPath;

    @GetMapping({"", "/"})
    public String list(Model model) {
        List<FileInfo> files = new ArrayList<>();
        File dir = new File(uploadPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] arr = dir.listFiles();
            if (arr != null) {
                for (File f : arr) {
                    if (f.isFile()) {
                        files.add(new FileInfo(f.getName(), f.length(), f.getName(),
                                "/uploads/" + f.getName()));
                    }
                }
            }
        }
        model.addAttribute("list", files);
        model.addAttribute("total", files.size());
        return "admin/media-list";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes ra) throws IOException {
        if (file.isEmpty()) {
            ra.addFlashAttribute("flashError", "请选择文件");
            return "redirect:/admin/media";
        }
        String original = file.getOriginalFilename();
        String ext = (original != null && original.contains("."))
                ? original.substring(original.lastIndexOf('.') + 1).toLowerCase() : "";
        String name = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "_" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path dirPath = Paths.get(uploadPath);
        Files.createDirectories(dirPath);
        Files.copy(file.getInputStream(), dirPath.resolve(name));
        ra.addFlashAttribute("flashMessage", "上传成功");
        return "redirect:/admin/media";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String filename, RedirectAttributes ra) {
        // 防止路径穿越
        String safe = Paths.get(filename).getFileName().toString();
        File f = new File(uploadPath, safe);
        if (f.exists()) {
            f.delete();
            ra.addFlashAttribute("flashMessage", "删除成功");
        } else {
            ra.addFlashAttribute("flashError", "文件不存在");
        }
        return "redirect:/admin/media";
    }

    /** 媒体文件展示用 DTO。 */
    public record FileInfo(String name, long size, String ext, String url) {}
}
