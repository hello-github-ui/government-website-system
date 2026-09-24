package com.example.gov.service;

import com.example.gov.common.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据备份服务：将数据库全量导出为 SQL 文件（无需本机 mysqldump），
 * 支持导出、列表、下载、删除与上传恢复。
 *
 * <p>导出内容：建表语句（SHOW CREATE TABLE）+ 全表数据（INSERT）。
 * 恢复采用逐条执行方式，请谨慎操作（会覆盖现有数据）。</p>
 */
@Service
public class BackupService {

    private static final Logger log = LoggerFactory.getLogger(BackupService.class);

    /** 备份文件目录 */
    @Value("${gov.backup-path:./backup/}")
    private String backupPath;

    private final JdbcTemplate jdbcTemplate;

    public BackupService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** 忽略的系统表（备份时跳过） */
    private static final List<String> SKIP_TABLES = List.of();

    /**
     * 执行全库导出，返回生成的备份文件名。
     */
    public String exportDatabase() {
        try {
            Path dir = Paths.get(backupPath);
            Files.createDirectories(dir);
            String filename = "backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".sql";
            Path out = dir.resolve(filename);

            StringBuilder sb = new StringBuilder();
            sb.append("-- 政府官网系统数据库备份\n");
            sb.append("-- 导出时间: ").append(LocalDateTime.now()).append("\n");
            sb.append("SET NAMES utf8mb4;\n");
            sb.append("SET FOREIGN_KEY_CHECKS = 0;\n\n");

            List<String> tables = jdbcTemplate.queryForList("SHOW TABLES", String.class);
            for (String table : tables) {
                if (SKIP_TABLES.contains(table)) {
                    continue;
                }
                sb.append("-- ----------------------------\n-- 表结构: ").append(table).append("\n-- ----------------------------\n");
                sb.append("DROP TABLE IF EXISTS `").append(table).append("`;\n");
                List<Map<String, Object>> creates = jdbcTemplate.queryForList("SHOW CREATE TABLE `" + table + "`");
                if (!creates.isEmpty() && creates.get(0).containsKey("Create Table")) {
                    sb.append(creates.get(0).get("Create Table")).append(";\n\n");
                }

                sb.append("-- 表数据: ").append(table).append("\n");
                appendTableData(sb, table);
            }
            sb.append("SET FOREIGN_KEY_CHECKS = 1;\n");

            Files.writeString(out, sb.toString(), StandardCharsets.UTF_8);
            log.info("数据库备份完成: {}", filename);
            return filename;
        } catch (Exception e) {
            log.error("数据库备份失败", e);
            throw new BizException("数据库备份失败: " + e.getMessage());
        }
    }

    /**
     * 拼接某张表的所有 INSERT 语句。
     */
    private void appendTableData(StringBuilder sb, String table) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM `" + table + "`");
        if (rows.isEmpty()) {
            return;
        }
        // 取列名（保持表顺序）
        List<String> columns = new ArrayList<>();
        rows.get(0).keySet().forEach(c -> columns.add(c));
        for (Map<String, Object> row : rows) {
            sb.append("INSERT INTO `").append(table).append("` (`")
                .append(String.join("`,`", columns)).append("`) VALUES (");
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                Object v = row.get(columns.get(i));
                sb.append(escapeValue(v));
            }
            sb.append(");\n");
        }
        sb.append("\n");
    }

    /**
     * SQL 值转义。
     */
    private String escapeValue(Object v) {
        if (v == null) {
            return "NULL";
        }
        if (v instanceof Number) {
            return v.toString();
        }
        if (v instanceof Boolean b) {
            return b ? "1" : "0";
        }
        if (v instanceof Timestamp ts) {
            return "'" + ts.toLocalDateTime() + "'";
        }
        String s = v.toString();
        // 转义单引号与反斜杠，避免破坏 SQL
        s = s.replace("\\", "\\\\").replace("'", "\\'");
        return "'" + s + "'";
    }

    /**
     * 列出备份文件（名称、大小、生成时间，按时间倒序）。
     */
    public List<BackupFile> listFiles() {
        List<BackupFile> result = new ArrayList<>();
        File dir = new File(backupPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.endsWith(".sql"));
            if (files != null) {
                for (File f : files) {
                    result.add(new BackupFile(f.getName(), f.length(), formatSize(f.length()),
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(f.lastModified())));
                }
            }
        }
        result.sort((a, b) -> b.name().compareTo(a.name()));
        return result;
    }

    /**
     * 删除备份文件。
     */
    public boolean delete(String filename) {
        String safe = Paths.get(filename).getFileName().toString();
        File f = new File(backupPath, safe);
        return f.exists() && f.delete();
    }

    /**
     * 从上传的 SQL 文件恢复数据库（逐条执行，忽略注释行）。
     *
     * <p>危险操作：会覆盖现有表数据。调用方需在前端二次确认。</p>
     */
    public int restore(String sqlContent) {
        int count = 0;
        // 按分号切分语句（简单解析，不处理存储过程）
        for (String raw : sqlContent.split(";")) {
            String stmt = raw.trim();
            if (stmt.isEmpty()) {
                continue;
            }
            // 去掉注释行
            StringBuilder clean = new StringBuilder();
            for (String line : stmt.split("\n")) {
                String l = line.trim();
                if (l.startsWith("--") || l.startsWith("#")) {
                    continue;
                }
                clean.append(line).append("\n");
            }
            String sql = clean.toString().trim();
            if (sql.isEmpty()) {
                continue;
            }
            jdbcTemplate.execute(sql);
            count++;
        }
        return count;
    }

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
     * 备份文件信息 DTO。
     */
    public record BackupFile(String name, long size, String sizeText, String time) {
    }
}
