package com.example.gov.controller.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * 后台媒体库控制器（文件上传/查看/筛选/删除）。
 *
 * <p>对齐 PHP 版功能：支持按类型（图片/视频/音频/文档/其他）筛选、
 * 图片缩略图预览、文件大小格式化、上传时间显示、复制链接与分页。</p>
 */
@Controller
@RequestMapping("/admin/media")
@Slf4j
public class AdminMediaController {

    @Value("${gov.upload-path:./uploads/}")
    private String uploadPath;

    /** 每页条数（与 PHP 版一致） */
    private static final int PAGE_SIZE = 20;

    /** 允许上传的扩展名白名单 */
    private static final List<String> ALLOWED_EXT = List.of(
        "jpg", "jpeg", "png", "gif", "webp", "bmp",
        "mp4", "avi", "mov", "wmv", "flv",
        "mp3", "wav", "ogg", "wma",
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt");

    /** 图片扩展名 */
    private static final List<String> IMAGE_EXT = List.of("jpg", "jpeg", "png", "gif", "webp", "bmp");
    /** 视频扩展名 */
    private static final List<String> VIDEO_EXT = List.of("mp4", "avi", "mov", "wmv", "flv");
    /** 音频扩展名 */
    private static final List<String> AUDIO_EXT = List.of("mp3", "wav", "ogg", "wma");
    /** 文档扩展名 */
    private static final List<String> DOC_EXT = List.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt");

    /**
     * 媒体列表：支持 type 筛选与分页。
     */
    @GetMapping({"", "/"})
    public String list(@RequestParam(defaultValue = "") String type,
                       @RequestParam(defaultValue = "1") int page,
                       Model model) {
        List<MediaItem> files = new ArrayList<>();
        File dir = new File(uploadPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] arr = dir.listFiles();
            if (arr != null) {
                for (File f : arr) {
                    if (!f.isFile()) {
                        continue;
                    }
                    String ext = extOf(f.getName());
                    String fileType = getFileType(ext);
                    if (type.isEmpty() || type.equals(fileType)) {
                        files.add(new MediaItem(
                            f.getName(),
                            f.length(),
                            formatSize(f.length()),
                            fileType,
                            ext,
                            "/uploads/" + f.getName(),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(f.lastModified())
                        ));
                    }
                }
            }
        }
        // 按时间倒序
        files.sort(Comparator.comparing(MediaItem::time).reversed());

        int total = files.size();
        int totalPage = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        int offset = (page - 1) * PAGE_SIZE;
        List<MediaItem> list = total == 0 ? List.of()
            : files.subList(Math.min(offset, total), Math.min(offset + PAGE_SIZE, total));

        model.addAttribute("list", list);
        model.addAttribute("page", page);
        model.addAttribute("total", total);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("type", type);
        return "admin/media-list";
    }

    /**
     * 上传文件（含扩展名白名单与大小校验）。
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes ra) throws IOException {
        if (file.isEmpty()) {
            ra.addFlashAttribute("flashError", "请选择文件");
            return "redirect:/admin/media";
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            ra.addFlashAttribute("flashError", "文件大小不能超过 10MB");
            return "redirect:/admin/media";
        }
        String original = file.getOriginalFilename();
        String ext = extOf(original == null ? "" : original);
        if (!ALLOWED_EXT.contains(ext)) {
            ra.addFlashAttribute("flashError", "不支持的文件类型: " + ext);
            return "redirect:/admin/media";
        }
        String name = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            + "_" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path dirPath = Paths.get(uploadPath);
        Files.createDirectories(dirPath);
        Files.copy(file.getInputStream(), dirPath.resolve(name));
        ra.addFlashAttribute("flashMessage", "上传成功");
        return "redirect:/admin/media";
    }

    /**
     * 删除文件（防路径穿越）。
     */
    @PostMapping("/delete")
    public String delete(@RequestParam String filename, RedirectAttributes ra) {
        log.info("[{}] AdminMediaController.delete 调用, 参数: filename={}", Thread.currentThread().getName(), filename);
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

    /** 解析扩展名（小写）。 */
    private String extOf(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx < 0 ? "" : filename.substring(idx + 1).toLowerCase();
    }

    /** 依据扩展名归类文件类型（对齐 PHP getFileType）。 */
    private String getFileType(String ext) {
        if (IMAGE_EXT.contains(ext)) {
            return "image";
        }
        if (VIDEO_EXT.contains(ext)) {
            return "video";
        }
        if (AUDIO_EXT.contains(ext)) {
            return "audio";
        }
        if (DOC_EXT.contains(ext)) {
            return "document";
        }
        return "other";
    }

    /** 格式化文件大小（对齐 PHP formatSize）。 */
    private String formatSize(long size) {
        String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        double s = size;
        while (s >= 1024 && unitIndex < units.length - 1) {
            s /= 1024;
            unitIndex++;
        }
        return String.format("%.2f %s", s, units[unitIndex]);
    }

    /**
     * 媒体文件展示用 DTO。
     */
    public record MediaItem(String name, long size, String sizeText, String type,
                            String ext, String url, String time) {
    }
}
