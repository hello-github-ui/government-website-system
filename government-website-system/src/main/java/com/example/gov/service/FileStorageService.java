package com.example.gov.service;

import com.example.gov.common.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 文件存储服务：统一处理上传文件落盘（用于媒体库、公告/政策/文书附件等）。
 *
 * <p>文件按“日期_UUID.扩展名”重命名保存，避免重名冲突与路径注入。</p>
 */
@Slf4j
@Service
public class FileStorageService {

    @Value("${gov.upload-path:./uploads/}")
    private String uploadPath;

    /** 允许的扩展名白名单 */
    private static final List<String> ALLOWED_EXT = List.of(
        "jpg", "jpeg", "png", "gif", "webp", "bmp",
        "mp4", "avi", "mov", "wmv", "flv",
        "mp3", "wav", "ogg", "wma",
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt");

    /**
     * 保存上传文件，返回生成的存储文件名。
     *
     * @param file   上传文件
     * @param maxMb  允许的最大体积（MB）
     */
    public String store(MultipartFile file, int maxMb) throws IOException {
        String thread = Thread.currentThread().getName();
        if (file == null || file.isEmpty()) {
            log.warn("[{}] 文件上传失败(文件为空)", thread);
            throw new BizException("请选择文件");
        }
        if (file.getSize() > maxMb * 1024L * 1024L) {
            log.warn("[{}] 文件上传失败(超过大小限制) size={}B, 限制={}MB",
                thread, file.getSize(), maxMb);
            throw new BizException("文件大小不能超过 " + maxMb + "MB");
        }
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = extOf(original);
        if (!ALLOWED_EXT.contains(ext)) {
            log.warn("[{}] 文件上传失败(类型不允许) ext={}, original={}", thread, ext, original);
            throw new BizException("不支持的文件类型: " + ext);
        }
        String name = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            + "_" + UUID.randomUUID().toString().replace("-", "") + "." + ext;
        Path dirPath = Paths.get(uploadPath);
        Files.createDirectories(dirPath);
        Files.copy(file.getInputStream(), dirPath.resolve(name));
        log.info("[{}] 文件上传成功 name={}, original={}, size={}B",
            thread, name, original, file.getSize());
        return name;
    }

    /** 解析扩展名（小写，不含点）。 */
    private String extOf(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx < 0 ? "" : filename.substring(idx + 1).toLowerCase();
    }
}
